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

    public void addKill() {
        kills++;
    }
    public void removeKill() {
        kills--;
    }
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
        return null;
    }
}
