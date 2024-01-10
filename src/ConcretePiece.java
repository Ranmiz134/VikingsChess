public abstract class ConcretePiece implements Piece {

    private Player owner;
    private String type;
    private Position position;

    /**
     * Constructor for AbstractPiece.
     *
     * owner The player who owns this piece.
     * type  A Unicode character representing the type of this piece.
     * position The initial position of the piece on the bord.
     */
    public ConcretePiece(Player owner, String type, Position position) {
        this.owner = owner;
        this.type = type;
        this.position = position;
    }

    /**
     * Get the player who owns the piece.
     *
     * @return The player who is the owner of this game piece.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Get the current position of the piece on the board.
     *
     * @return The current position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set the position of the piece to a new position.
     *
     * newPosition The new position.
     */
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    /**
     *
     * @return A Unicode character representing the type of this game piece
     *
     */
    @Override
    public String getType() {
        return type;
    }
}
