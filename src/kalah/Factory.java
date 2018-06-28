package kalah;

import com.qualitascorpus.testsupport.IO;
import kalah.board.*;
import kalah.view.IGameUI;
import kalah.view.KalahBoardUI;

import java.util.ArrayList;

public class Factory implements IFactory {

    public IPit createPit(IPlayer player, int seed_count) {

        return new Pit(player, seed_count);
    }

    public IStore createStore(IPlayer player, int seed_count) {
        return new Store(player, seed_count);
    }

    public IHouse createHouse(IPlayer player, int seed_count) {
        return new House(player, seed_count);
    }

    public IPlayer createPlayer(int player_id, int player_store_index) {
        return new Player(player_id, player_store_index);
    }

    public IGameBoard createBoard(int no_of_players, int no_of_houses, int player_turn, int init_house_seeds, int init_store_seeds, IO io, IFactory factory) {
        return new KalahBoard(no_of_players, no_of_houses,  player_turn, init_house_seeds, init_store_seeds, io, factory);
    }

    public IGameUI createGameUI(IO io) {
        return new KalahBoardUI(io);
    }

    public ArrayList<IPit> createArrayList() {
        return new ArrayList<IPit>();
    }


}
