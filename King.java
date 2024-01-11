public class King extends ConcretePiece {

    //track if the King has moved.
    private boolean hasMoved;

    /**
     * Constructor for pawn.
     *
     * owner The player who owns this piece-Player 1.
     * type the symbol of pawn.
     *
     */
    public King(String type,Position position) {
        super(new ConcretePlayer(true),"♚", position);
        this.hasMoved = false;
    }

    /**
     * Check if the King has moved.
     *
     * @return true if the King has moved, false otherwise.
     */
    public boolean hasMoved() {
        return this.hasMoved;
    }

    /**
     * Move the King to a new position.?????
     */
    public void move() {
        // Additional logic for moving a pawn, updating position....
        this.hasMoved = true;
    }

    /**
     * @return the symbol of King piece.
     */
    public String getType() {
        return "♚";  // Unicode character for King.
    }
}
