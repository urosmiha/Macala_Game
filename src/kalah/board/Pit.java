package kalah.board;

public class Pit implements IPit {

    protected IPlayer player;
    protected int seed_count;

    public Pit(IPlayer player, int seed_count) {

        this.player = player;
        this.seed_count = seed_count;
    }

    public IPlayer getPlayer() { return player; }

    public int getSeedCount() { return seed_count; }

    public void addSeed() { this.seed_count++; }

    public void addSeed(int seeds) { this.seed_count += seeds; }
}
