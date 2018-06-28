package kalah.board;

import java.util.ArrayList;

import com.qualitascorpus.testsupport.IO;
import kalah.Factory;
import kalah.IFactory;
import kalah.view.IGameUI;
import kalah.view.KalahBoardUI;

public class KalahBoard implements IGameBoard {

	protected int no_of_players;
	protected int no_of_player_houses;  // Stores the number of houses for each player
	protected int no_of_houses;         // Stores the total number of houses (does not include stores)

	private int player_turn;
	protected IGameUI game_view;
	private IFactory factory;

	ArrayList<IPit> game_fields;

	public KalahBoard(int no_of_players, int no_of_houses, int player_turn, int init_house_seeds, int init_store_seeds, IO io, IFactory factory) {

		this.factory = factory;	// Use factory instance to create new objects that Kalah board needs.

		game_fields = this.factory.createArrayList();

		this.game_view = this.factory.createGameUI(io);		// Game board now depends on Game UI interface not the actual object. The io object has been injected into GameUI.
		this.player_turn = player_turn;

		this.no_of_houses = no_of_houses;
		this.no_of_players = no_of_players;
		this.no_of_player_houses = this.no_of_houses / this.no_of_players;

		initializeGameBoard(init_house_seeds, init_store_seeds);
	}

	private void initializeGameBoard(int init_house_seeds, int init_store_seeds) {
		// For each player add a House based on the number of homes specified.
		for(int i = 1; i <= no_of_players; i++) {
			IPlayer new_player = this.factory.createPlayer(i,(no_of_houses-1)+i);	// Kalah Board now depends on PLayer Interface rather than the actual player object
			for(int j = 0; j < no_of_player_houses; j++) {
				this.game_fields.add(this.factory.createHouse(new_player,init_house_seeds));		// Same applies to House and Store below
			}
		}

		// For each player add a single Store.
		for(int i = 1; i <= no_of_players; i++) {
			IPlayer new_player = this.factory.createPlayer(i,(no_of_houses-1)+i);
			this.game_fields.add(this.factory.createStore(new_player, init_store_seeds));
		}
	}

	// Takes the index of the house the player has selected and returns the next player's turn.
	// If input is -1 that means the player has selected "q".
	// Return 0 if there is no more moves to make.
	public int playTurn(int index) {

		if(index == -1) {
			game_view.printGameOver();
			formatGameBoard();
		} else {

			// Convert the selected index to corresponding array list index
			int tmp_index = ((player_turn-1) * no_of_player_houses) + (index-1);

			if (isValidTurn(tmp_index)) {

				// Make move and get last index played
				int last_index = moveSeeds(tmp_index);
				// Use the last index to steal opponents seeds if possible.
				if (isCapture(last_index)) { captureSeeds(last_index); }

				formatGameBoard();
				updatePlayerTurn(last_index);

				// If there are no further moves to be made - end game
				if (isGameOver()) {
					game_view.printGameOver();
					formatGameBoard();

					addRemainingSeeds();
					formatWinningScore(CheckWhoWon());
					//Game has finished
					this.player_turn = 0;
				}
			} else {
				game_view.printInvalidMove();
				formatGameBoard();
			}
		}
		return player_turn;
	}

	// Check if the field player placed last seed in is the House field.
	// Which means it's the next players turn - set player turn to next player.
	private void updatePlayerTurn(int last_index) {
		if(last_index < no_of_houses) {
			if(player_turn == no_of_players) {
				player_turn = 1;
			} else {
				player_turn++;
			}
		}
	}

	// Check if selected index is in range of linked list and if selected field has seeds in it.
	private boolean isValidTurn(int index) {
		if(isValidHouse(index)) {
			if(game_fields.get(index).getSeedCount() > 0) {
				return true;
			}
		}
		return false;
	}

	// Check if the selected field is pit (i.e not store).
	private boolean isValidHouse(int index) {
		if(index >= 0  && index < no_of_houses) {
			return true;
		}
		return false;
	}

	// Returns true if player can capture the seeds. Under few circumstances:
	// 1st check if the last seed was placed in House (i.e. not Store) and it belongs to the player who played last seed.
	// Then check is the pit was initially empty (i.e. there is one seed in it now).
	private boolean isCapture(int last_index_played) {

		if(isValidHouse(last_index_played)) {
			if(game_fields.get(last_index_played).getPlayer().getPlayerID() == player_turn && game_fields.get(last_index_played).getSeedCount() == 1) {
				return true;
			}
		}
		return false;
	}

	// Return true if the current player cannot make move (i.e. all of their Houses are empty).
	private boolean isGameOver() {

		int start_index = no_of_player_houses * (player_turn-1);

		for(int i = start_index; i < (start_index + no_of_player_houses); i++) {
			if(game_fields.get(i).getSeedCount() != 0) {
				return false;
			}
		}
		return true;
	}

	// For each player add seeds form their Houses to their Stores.
	private void addRemainingSeeds() {
		int remain_seeds = 0;
		int start_index = 0;

		for(int i = 1; i <= no_of_players; i++) {
			for(int j = start_index; j < (i * no_of_player_houses); j++) {
				remain_seeds += game_fields.get(j).getSeedCount();
			}
			game_fields.get(i+(no_of_houses-1)).addSeed(remain_seeds);
			remain_seeds = 0;
			start_index += no_of_player_houses;
		}
	}

	// Takes the array list index and places the seed in each field in anti-clockwise direction for each seed in start index.
	// Number of seeds in selected house indicates in how many fields we need to place the seed.
	private int moveSeeds(int index) {

		int play_count = game_fields.get(index).getSeedCount();
		((House)game_fields.get(index)).removeAllSeeds();

		/*
		 * Since the houses are stored first in the array list and then stores are at the end of the array
		 * We need to loop through the houses for the first player and then move to the end of the array to check the player's store.
		 * After that we need to go back to where we left off to check the houses for the next player.
	     *
	     * */

		int last_index = index;
		int current_index = index;
		int prev_index = index;

		while (play_count != 0) {

			if((current_index+1) % no_of_player_houses == 0) {
				prev_index = current_index;
				current_index = (current_index / no_of_player_houses) + no_of_houses;
			} else if(current_index >= no_of_houses) {
				if((prev_index + 1) == no_of_houses) {
					current_index = 0;
				} else {
					current_index = prev_index + 1;
				}
			} else {
				current_index++;
			}

			// Only add seed to houses and store that belongs to current player.
			if((game_fields.get(current_index).getPlayer().getPlayerID() == player_turn && current_index >= no_of_houses) || current_index < no_of_houses) {

				game_fields.get(current_index).addSeed();
				play_count--;
			}
			last_index = current_index;
		}
		return last_index;
	}

	// Get the seeds of the opposing index and place them in the current players store.
	// Only if the captured house is not empty.
	private void captureSeeds(int index) {

		int facing_index = (no_of_houses-1) - index;

		if(game_fields.get(facing_index).getSeedCount() != 0) {

			int stolen_seeds = game_fields.get(facing_index).getSeedCount();
			((House) game_fields.get(index)).removeAllSeeds();
			((House) game_fields.get(facing_index)).removeAllSeeds();

			int player_store_index = game_fields.get(index).getPlayer().getPlayerStoreIndex();
			game_fields.get(player_store_index).addSeed(stolen_seeds + 1);
		}
	}

	// Check the stores of all players and return the player with the highest store.
	private int CheckWhoWon() {

		int winner = 0;
		int high_score = 0;

		for(int i=0; i < no_of_players; i++) {
			if(game_fields.get(no_of_houses+i).getSeedCount() > high_score) {
				high_score = game_fields.get(no_of_houses+i).getSeedCount();
				winner = game_fields.get(no_of_houses+i).getPlayer().getPlayerID();
			} else if(game_fields.get(no_of_houses+i).getSeedCount() == high_score) {
				winner = 0; // Tie game
			}
		}
		return winner;
	}

	// Format the fields for each player to be displayed.
	public void formatGameBoard() {

		int i = 1;

		String bottom_row = "| " + String.format("%2d",game_fields.get(no_of_houses+1).getSeedCount()) + " | ";
		for(int j = 0; j < no_of_player_houses; j++) {
			bottom_row += i + "[" + String.format("%2d", game_fields.get(j).getSeedCount()) + "]" + " | ";
			i++;
		}
		bottom_row += "P1 |";

		i = 6;
		String top_row = "| P2 | ";
		for(int j = (2*no_of_player_houses)-1; j >= no_of_player_houses; j--) {
			top_row += i + "[" + String.format("%2d", game_fields.get(j).getSeedCount()) + "]" + " | ";
			i--;
		}
		top_row +=  String.format("%2d",game_fields.get(no_of_houses).getSeedCount()) + " |";

		game_view.printGameBoard(top_row,bottom_row);
	}

	public int getPlayerInput(int player_turn) {

        String ask_for_input = "Player P" + player_turn + "'s turn - Specify house number or 'q' to quit: ";
        return game_view.printPlayerTurn(ask_for_input,1,6,-1,"q");
    }

	public void formatWinningScore(int win_player)  {

	    String score_board;

        for(int i = 1; i <= no_of_players; i++) {
		    score_board = "\tplayer " + i + ":" + game_fields.get(i + (no_of_houses-1)).getSeedCount();
		    game_view.printWinningScore(score_board);
	    }

	    String winner;
	    if(win_player == 0) {
		    winner = "A tie!";
	    } else {
		    winner = "Player " + win_player + " wins!";
	    }
	    game_view.printWinner(winner);
    }
}
