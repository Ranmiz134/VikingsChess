import java.util.LinkedList;
import java.util.List;

public class Position {

    private int row;
    private int col;
    private List<Piece> pieces;

    /**
     * Constructor for Position.
     *
     * row The row index of the position.
     * col The column index of the position.
     * A linked record of pieces will be built.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
        this.pieces = new LinkedList<>();
    }

    /**
     * Get the row index of the position.
     *
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column index of the position.
     *
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Get the list of pieces.
     *
     * @return The column index.
     */
    public List<Piece> getPieces() {
        return pieces;
    }

    public void raisePiece(ConcretePiece piece) {
        if (!pieces.contains(piece)) {
            pieces.add(piece);
        }
        piece.updatePosition(this);
    }

    /**
     * Returns the position of the tool by (row, col) on the board
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
