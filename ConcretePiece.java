//Shmuel Ben-Atar 208007138
//Ran Mizrahi 314809625
//ConcretePiece.java

import java.util.ArrayList;
import java.util.List;

public abstract class ConcretePiece implements Piece {

    protected Player owner; //player one or player two
    private final List<Position> positions = new ArrayList<>();
    protected int sn; //serial number
    protected int dis; //distance

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
        return this.positions.get(positions.size() - 1);
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

    //get and set for distance
    public int getDis() {
        return this.dis;
    }

    public void setDis(int newDistance) {
        this.dis = newDistance;
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


}
