package kalah.view;

import com.qualitascorpus.testsupport.IO;

public class KalahBoardUI implements IGameUI {

	private IO io;

	public KalahBoardUI(IO io) {
		this.io = io;
	}

	public void printGameOver() { io.println("Game over"); }

	public void printGameBoard(String top_row, String bottom_row) {

		printBorder();
		io.println(top_row);
		io.println("|    |-------+-------+-------+-------+-------+-------|    |");
		io.println(bottom_row);
		printBorder();
	}

	public void printBorder() {
		io.println("+----+-------+-------+-------+-------+-------+-------+----+");
	}

	public int printPlayerTurn(String ask_player_input, int lower_limit, int upper_limit, int feedback, String exit) {

		return io.readInteger(ask_player_input, lower_limit, upper_limit, feedback, exit);
	}

	public void printWinningScore(String score_board) {	io.println(score_board); }

	public void printWinner(String winner) { io.println(winner); }

	public void printInvalidMove() { io.println("House is empty. Move again.");	}
}
