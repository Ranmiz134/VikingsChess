public class Pawn extends ConcretePiece {

    //track if the pawn has moved.
    private boolean hasMoved;

    /**
     * Constructor for pawn.
     *
     * owner The player who owns this piece.
     * type the symbol of pawn.
     *
     */
    public Pawn(Player owner, String type,Position position) {
        super(owner,"♟", position);
        this.hasMoved = false;
    }

    /**
     * Check if the pawn has moved.
     *
     * @return true if the pawn has moved, false otherwise.
     */
    public boolean hasMoved() {
        return this.hasMoved;
    }

    /**
     * Move the pawn to a new position.
     */
    public void move() {
        // Additional logic for moving a pawn, updating position....
        this.hasMoved = true;
    }

    /**
     * @return the symbol of pawn piece.
     */
    public String getType() {
        return "♟";  // Unicode character for pawn.
    }
}
