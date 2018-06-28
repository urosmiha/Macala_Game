package kalah.board;

public interface IPit {


    IPlayer getPlayer();

    int getSeedCount();

    void addSeed();

    void addSeed(int seeds);
}
