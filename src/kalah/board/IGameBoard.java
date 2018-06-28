package kalah.board;

public interface IGameBoard {

    int playTurn(int index);

    void formatGameBoard();

    int getPlayerInput(int player_turn);

    void formatWinningScore(int win_player);
}

