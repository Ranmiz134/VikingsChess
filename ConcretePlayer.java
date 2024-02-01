//Shmuel Ben-Atar 208007138
//Ran Mizrahi 314809625
//ConcretePlayer.java

public class ConcretePlayer implements Player {

    private boolean isPlayerOne; //player one-protector, player two-attack
    private int wins; //The player's number of wins

    /**
     * Constructor for ConcretePlayer.
     *
     * isPlayerOne true if the player is player 1, false otherwise.
     */
    public ConcretePlayer(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        this.wins = 0;
    }

    @Override
    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    //get for wins
    @Override
    public int getWins() {
        return this.wins;
    }

    public void win() {
        this.wins++;
    }
}
