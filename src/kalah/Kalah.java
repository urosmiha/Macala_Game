package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.board.IGameBoard;
import kalah.board.KalahBoard;

public class Kalah {
	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}

	private static final int NO_OF_PLAYERS = 2;
	private static final int NO_OF_HOMES = 12;

	private static final int INIT_HOUSE_SEEDS = 4;
	private static final int INIT_STORE_SEEDS = 0;

	private IGameBoard game_board;
	private int userInput = 0;
	private int current_player_turn = 1;

	// Create factory here so only the top level module (Kalah) depends on it.
	// Later use dependency injection so other modules can use it.
	private IFactory factory = new Factory();

	public void play(IO io) {
		game_board = factory.createBoard(NO_OF_PLAYERS, NO_OF_HOMES, current_player_turn, INIT_HOUSE_SEEDS, INIT_STORE_SEEDS, io, factory);	// dependency injection of io and factory
		game_board.formatGameBoard();

		// -1 = "q" - quit game
		// if current player turn is zero that indicates that no more moves can be made - game ended.
		while(userInput != -1 && current_player_turn != 0) {

			userInput = game_board.getPlayerInput(current_player_turn);
			current_player_turn = game_board.playTurn(userInput);
		}
	}
}
