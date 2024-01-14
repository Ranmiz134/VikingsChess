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
     * Set the position to a new row and column.
     *
     * row The new row index.
     * col The new column index.
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Check if two positions are equal.
     *
     * other The other position to compare.
     * @return true if the positions are equal, false otherwise.
     */
    public boolean equals(Position other) {
        return this.row == other.row && this.col == other.col;
    }

    /**
     * We will get a piece and remove him from the list
     */
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    /**
     * Returns the position of the tool by (row, col) on the board
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
