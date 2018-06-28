package kalah;

import com.qualitascorpus.testsupport.IO;
import kalah.board.*;
import kalah.view.IGameUI;
import kalah.view.KalahBoardUI;

import java.util.ArrayList;

public interface IFactory {

    IPit createPit(IPlayer player, int seed_count);

    IStore createStore(IPlayer player, int seed_count);

    IHouse createHouse(IPlayer player, int seed_count);

    IPlayer createPlayer(int player_id, int player_store_index);

    IGameBoard createBoard(int no_of_players, int no_of_houses, int player_turn, int init_house_seeds, int init_store_seeds, IO io, IFactory factory);

    IGameUI createGameUI(IO io);

    public ArrayList<IPit> createArrayList();
}
