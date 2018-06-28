package kalah.board;

public class House extends Pit implements IHouse{

    public House(IPlayer player, int seed_count) {
        super(player, seed_count);
        this.player = player;
        this.seed_count = seed_count;
    }

    public void removeAllSeeds() { this.seed_count = 0; }
}
