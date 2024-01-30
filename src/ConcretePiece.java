import java.util.ArrayList;
import java.util.List;

public abstract class ConcretePiece implements Piece {

    protected Player owner;
    private final List<Position> positions = new ArrayList<>();
    protected int sn; //serial number
    protected int dis; //distance
    protected int kills;

    /**
     * Get the player who owns the piece.
     *
     * @return The player who is the owner of this game piece.
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Get the current position of the piece on the board. (the last position in the list)
     *
     * @return The current position.
     */
    public Position getPosition() {
        return this.positions.getLast();
    }

    /**
     * Get the full list of the positions of the piece on the board.
     *
     * @return The List positions.
     */
    public List<Position> getAllPositions() {
        return this.positions;
    }

    /**
     * Update the position of the piece and Add the new position of the piece to the list.
     *
     * newPosition The new position.
     */
    public void updatePosition(Position newPosition) {
        this.addPosition(newPosition);
    }

    /**
     * Add the new position of the piece to the list.
     *
     * newPosition The new position.
     */
    public void addPosition(Position newPosition) {
        this.positions.add(newPosition);
    }

    /**
     * Get and Set to sn.
     */
    public void setSn(int num) {
        this.sn = num;
    }

    public int getNumber() {
        return this.sn;
    }

/*
    public String getDistStr() {
        return this.getDist() + " squares";
    }
*/

    /**
     * A function that calculates the distance of the game from the boundaries of the board and brings back the distance
     */
    private int distanceCalculation() {
        int i, x, x1, y, y1;
        for(i = 0; i < this.positions.size() - 1; ++i) {
            x = this.positions.get(i).getRow();
            y = this.positions.get(i).getCol();
            x1 = this.positions.get(i + 1).getRow();
            y1 = this.positions.get(i + 1).getCol();
            this.dis += Math.abs(x - x1) + Math.abs(y - y1);
        }

        return this.dis;
    }

    /**
     * @return 0 If the distance of the tool is 0, otherwise you will return the calculation of the distance
     */
    public int getDis() {
        if(this.dis == 0)
            return this.dis;
        return this.distanceCalculation();
    }

    /**
     * @return A Unicode character representing the type of this game piece
     */
    @Override
    abstract public String getType();

    /**
     * print A Unicode character representing the type of this game piece
     */
    @Override
    abstract public String toString();

    /**
     * The function adds a kill to the pawn
     */
    public void addKill() {
        kills++;
    }

    /**
     * The function remove a kill to the pawn
     */
    public void removeKill() {
        kills--;
    }

    /**
     * return how many kills their is to the pawn
     */
    public int getKills() {
        return kills;
    }
}
