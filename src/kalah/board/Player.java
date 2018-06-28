package kalah.board;

public class Player implements IPlayer {

	int player_id;
	int player_store_index;

	public Player(int player_id, int player_store_index) {
		this.player_id = player_id;
		this.player_store_index = player_store_index;
	}

	public int getPlayerID() { return this.player_id; }

	// Returns the index of the array for the player's store.
	public int getPlayerStoreIndex() { return this.player_store_index; }

}
