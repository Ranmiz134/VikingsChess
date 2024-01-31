public class King extends ConcretePiece {

    /**
     * Constructor for pawn.
     *
     * owner The player who owns this piece-Player 1.
     * type the symbol of pawn.
     *
     */

    public King(Player playerOne) {
        this.owner = playerOne;
        this.dis = 0;
    }

    /**
     * @return the symbol of King piece.
     */
    public String getType() {
        return "♔";  // Unicode character for King.
    }

    /**
     * @return the symbol of King piece.
     */
    @Override
    public String toString() {
        return "K"  + sn + ": ";
    }
}
