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

    public void raisePiece(ConcretePiece piece) {
        pieces.add(piece);
        piece.updatePosition(this);
    }

    /**
     * We will get a piece and remove him from the list
     */
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    // Method to get the number of pieces at this position
    public int getPieceSize() {
        if (pieces == null) {
            return 0; // Return 0 if the list is null or empty
        } else {
            return pieces.size(); // Return the size of the list
        }
    }

    public String piecesStr() {
        String result = "";

        if (pieces == null || pieces.isEmpty()) {
            return "No pieces"; // Return a message indicating no pieces are present
        } else {
            for (Piece piece : pieces) {
                result += piece.getType() + ", "; // Assuming Piece class has a method getType() to get the type
            }
            result = result.substring(0, result.length() - 2); // Remove the trailing comma and space
            return result;
        }
    }



    /**
     * Returns the position of the tool by (row, col) on the board
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
