package kalah.view;

public interface IGameUI {

	void printGameOver();

	 void printGameBoard(String top_row, String bottom_row);

	 void printBorder();

	 int printPlayerTurn(String ask_player_input, int lower_limit, int upper_limit, int feedback, String exit);

	 void printWinningScore(String score_board);

	 void printWinner(String winner);

	 void printInvalidMove();
}
