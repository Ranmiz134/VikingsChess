public class ConcretePlayer implements Player {

    private boolean isPlayerOne;
    private int wins;

    /**
     * Constructor for ConcretePlayer.
     *
     * isPlayerOne true if the player is player 1, false otherwise.
     */
    public ConcretePlayer(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        this.wins = 0;
    }

    /**
     *
     * @return true if the player is player 1, false otherwise.
     */
    @Override
    public boolean isPlayerOne() {
        return this.isPlayerOne;
    }

    /**
     * Get the number of wins achieved by the player in the game.
     *
     * @return The total number of wins by the player.
     */
    @Override
    public int getWins() {
        return this.wins;
    }

    /**
     * Increment the number of wins for the player.
     */
    public void incrementNumOfWins() {
        this.wins++;
    } //mayby we will change to set
}
