package kalah.board;

public class Store extends Pit implements IStore{

    public Store(IPlayer player, int seed_count) {
        super(player, seed_count);
        this.player = player;
        this.seed_count = seed_count;
    }
}
