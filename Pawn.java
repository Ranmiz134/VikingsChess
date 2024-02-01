//Shmuel Ben-Atar 208007138
//Ran Mizrahi 314809625
//Pawn.java

public class Pawn extends ConcretePiece {

private int kills;
    /**
     * Constructor for pawn.
     *
     * owner The player who owns this piece.
     * type the symbol of pawn.
     *
     */
    public Pawn(Player owner) {
        this.owner = owner;
        this.dis = 0;
        this.kills = 0;
    }

    /**
     * The function adds a kill to the pawn
     */
    public void addKill() {
        kills++;
    }

    /**
     * return how many kills their is to the pawn
     */
    public int getKills() {
        return kills;
    }

    /**
     * @return the symbol of pawn piece.
     */
    public String getType() {
        if(this.owner.isPlayerOne())
            return "♙";

        return "♟";
    }

    @Override
    public String toString() {
        if (owner.isPlayerOne())
            return "D"  + sn + ": ";

        return "A"  + sn + ": ";
    }
}
