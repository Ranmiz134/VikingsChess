//Shmuel Ben-Atar 208007138
//Ran Mizrahi 314809625
//GameLogic.java

import java.util.*;

public class GameLogic implements PlayableLogic {

    private ConcretePlayer playerOne;
    private ConcretePlayer playerTwo;
    private ConcretePiece[][] board;
    private Position[][] positions;
    private boolean secondPlayerTurn;
    private boolean gameFinished;
    private static int BOARD_SIZE = 11;
    private List<ConcretePiece> capturedPieces;
    private ArrayList<ConcretePiece> pieces = new ArrayList<>();
    private ConcretePlayer winner;
    private int[][] numOfPiece = new int[BOARD_SIZE][BOARD_SIZE];
    public GameLogic() {
        this.positions = new Position[BOARD_SIZE][BOARD_SIZE];
        this.secondPlayerTurn = true;
        this.playerOne = new ConcretePlayer(true);
        this.playerTwo = new ConcretePlayer(false);
        reset();
    }
    /**
     * Moves a piece from one position to another on the game board.
     *
     * @param a The initial position of the piece to be moved.
     * @param b The position where the piece will be moved to.
     * @return true if the move was executed successfully, false otherwise.
     */
    @Override
    public boolean move(Position a, Position b) {
        boolean playerTwoTurn = isSecondPlayerTurn();
        boolean playerOnePosition = getPieceAtPosition(a).getOwner().isPlayerOne();

        // Check if the move is invalid, or if it's player two's turn but the piece belongs to player one,
        // or if it's player one's turn but the piece belongs to player two
        if (isInvalidMove(a, b) || (playerTwoTurn && playerOnePosition) || (!playerTwoTurn && !playerOnePosition))
            return false;

        capturedPieces = new ArrayList<>();

        ConcretePiece movingPiece = movePiece(a, b);

        updatePositionHistory(b, movingPiece, a);

        checkKillAndVictory(b);

        switchTurn();

        boolean kingDied = isKingSurrounded();

        boolean allRedDied = blueEatAllRed();

        // If the game is finished, determine the winner and print the win message
        if(gameFinished) {
            if (kingDied)
                winner = playerTwo;
            else {
                if (kingWin() || allRedDied)
                    winner = playerOne;
           }

            winner.win();
            printWin();
        }

        return true;
    }

    /**
     * Retrieves the piece located at the specified position on the game board.
     *
     * @param position The position for which to retrieve the piece.
     * @return The piece located at the specified position.
     */
    @Override
    public Piece getPieceAtPosition(Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Retrieves the first player in the game.
     *
     * @return The first player in the game.
     */
    @Override
    public Player getFirstPlayer() {
        return playerOne;
    }

    /**
     * Retrieves the second player in the game.
     *
     * @return The second player in the game.
     */
    @Override
    public Player getSecondPlayer() {
        return playerTwo;
    }

    /**
     * Checks if the game has finished.
     *
     * @return true if the game has finished, false otherwise.
     */
    @Override
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Checks if it is currently the second player's turn.
     *
     * @return true if it is the second player's turn, false otherwise.
     */
    @Override
    public boolean isSecondPlayerTurn() {
        return secondPlayerTurn;
    }

    /**
     * Resets the game to its initial state.
     *
     * This method resets the game by clearing the board and re-initializing the pieces
     * for both players. It sets the game as unfinished and sets the second player's turn
     * as the starting turn.
     */
    @Override
    public void reset() {
        int i, j;
        // Reset game state
        gameFinished = false;
        secondPlayerTurn = true;
        // Clear the board and initialize a new array of ConcretePiece objects
        board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        //now we will create and place ich pawn first player one's pawns and than for player two's pawns.
        for(i = 4; i <= 6; i++) {
            for(j = 4; j <= 6; j++) {
                if( i !=5 || j !=5) {
                    board[i][j] = new Pawn(playerOne);
                    board[i][j].addPosition(new Position(i, j));
                    addStepToPos(i, j);
                    pieces.add(board[i][j]);
                }
                else {
                    board[i][j] = new King(playerOne);
                    board[i][j].addPosition(new Position(i, j));
                    addStepToPos(i, j);
                    pieces.add(board[i][j]);
                }
            }
        }
        board[5][3] = new Pawn(playerOne); board[5][3].addPosition(new Position(5, 3)); addStepToPos(5, 3);
        board[3][5] = new Pawn(playerOne); board[3][5].addPosition(new Position(3, 5)); addStepToPos(3, 5);
        board[5][7] = new Pawn(playerOne); board[5][7].addPosition(new Position(5, 7)); addStepToPos(5, 7);
        board[7][5] = new Pawn(playerOne); board[7][5].addPosition(new Position(7, 5)); addStepToPos(7, 5);
        pieces.add(board[5][3]);
        pieces.add(board[3][5]);
        pieces.add(board[5][7]);
        pieces.add(board[7][5]);

        //player two
        for(i = 3; i<=7; i++) {
            board[0][i] = new Pawn(playerTwo);
            board[0][i].addPosition(new Position(0, i));
            addStepToPos(0, i);
            pieces.add(board[0][i]);
        }

        for(i = 3; i <= 7; i++) {
            board[i][0] = new Pawn(playerTwo);
            board[i][0].addPosition(new Position(i, 0));
            addStepToPos(i, 0);
            pieces.add(board[i][0]);
        }

        for(i = 3; i <= 7; i++) {
            board[10][i] = new Pawn(playerTwo);
            board[10][i].addPosition(new Position(10, i));
            addStepToPos(10, i);
            pieces.add(board[10][i]);
        }

        for(i = 3; i <= 7; i++) {
            board[i][10] = new Pawn(playerTwo);
            board[i][10].addPosition(new Position(i, 10));
            addStepToPos(i, 10);
            pieces.add(board[i][10]);
        }

        board[1][5] = new Pawn(playerTwo); board[1][5].addPosition(new Position(1, 5)); addStepToPos(1, 5);
        board[5][1] = new Pawn(playerTwo); board[5][1].addPosition(new Position(5, 1)); addStepToPos(5, 1);
        board[9][5] = new Pawn(playerTwo); board[9][5].addPosition(new Position(9, 5)); addStepToPos(9, 5);
        board[5][9] = new Pawn(playerTwo); board[5][9].addPosition(new Position(5, 9)); addStepToPos(5, 9);
        pieces.add(board[1][5]);
        pieces.add(board[5][1]);
        pieces.add(board[9][5]);
        pieces.add(board[5][9]);

        //set for every piece sn
        board[3][0].setSn(1); board[4][0].setSn(2); board[5][0].setSn(3); board[6][0].setSn(4); board[7][0].setSn(5); board[5][1].setSn(6);
        board[0][3].setSn(7); board[10][3].setSn(8); board[0][4].setSn(9); board[10][4].setSn(10); board[0][5].setSn(11); board[1][5].setSn(12);
        board[9][5].setSn(13); board[10][5].setSn(14); board[0][6].setSn(15); board[10][6].setSn(16); board[0][7].setSn(17); board[10][7].setSn(18);
        board[5][9].setSn(19); board[3][10].setSn(20); board[4][10].setSn(21); board[5][10].setSn(22); board[6][10].setSn(23); board[7][10].setSn(24);
        board[5][3].setSn(1); board[4][4].setSn(2); board[5][4].setSn(3); board[6][4].setSn(4); board[3][5].setSn(5); board[4][5].setSn(6); board[5][5].setSn(7);
        board[6][5].setSn(8); board[7][5].setSn(9); board[4][6].setSn(10); board[5][6].setSn(11); board[6][6].setSn(12); board[5][7].setSn(13);

    }

    @Override
    public void undoLastMove() {

    }

    /**
     * Retrieves the size of the game board.
     *
     * @return The size of the game board.
     */
    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Checks if a given position is in one of the corners of the game board.
     *
     * @param position The position to check.
     * @return true if the position is in one of the corners, false otherwise.
     */
    public boolean checkCorner(Position position) {
        return (position.getRow() % 10 == 0 && position.getCol() % 10 == 0);
    }

    /**
     * Checks if a move from a source position to a destination position is invalid.
     *
     * @param source      The source position of the move.
     * @param destination The destination position of the move.
     * @return true if the move is invalid, false otherwise.
     */
    public boolean isInvalidMove(Position source, Position destination) {
        boolean playerTwoTurn = isSecondPlayerTurn();
        Player pieceOwner = getPieceOwner(source);

        //Check if it's the player two turn and the piece belongs to player one
        if (playerTwoTurn && pieceOwner.isPlayerOne())
            return true;

        //Check if it's the player one turn and the piece belongs to player two
        if (!playerTwoTurn && !pieceOwner.isPlayerOne())
            return true;

        // Check if the move is not vertical or horizontal
        if (!isVerticalOrHorizontalMove(source, destination))
            return true;

        // Check for pieces on the way
        if (piecesOnTheWay(source, destination))
            return true;

        // Check if the destination is a corner and the piece is not a King
        if (checkCorner(destination) && !(getPieceAtPosition(source) instanceof King))
            return true;

        return false;
    }

    /**
     * Checks if a move from a source position to a destination position is vertical or horizontal.
     *
     * @param source      The source position of the move.
     * @param destination The destination position of the move.
     * @return true if the move is vertical or horizontal, false otherwise.
     */
    public boolean isVerticalOrHorizontalMove(Position source, Position destination) {
        return source.getRow() == destination.getRow() || source.getCol() == destination.getCol();
    }

    /**
     * Checks if there are pieces obstructing the path from a source position to a destination position.
     *
     * @param source      The source position of the move.
     * @param destination The destination position of the move.
     * @return true if there are pieces obstructing the path, false otherwise.
     */
    public boolean piecesOnTheWay(Position source, Position destination) {
        int i, x1, x2, y1, y2, start, end;
        x1 = source.getRow();
        y1 = source.getCol();
        x2 = destination.getRow();
        y2 = destination.getCol();

        if (board[x2][y2] != null)
            return true;

        // Check if there are pieces on the way for a horizontal move
        if (y1 == y2) {
            start = Math.min(x1, x2) + 1;
            end = Math.max(x1, x2);
            for (i = start; i < end; i++) {
                if (board[i][y1] != null)
                    return true; // There is a piece on the way
            }
        }

        // Check if there are pieces on the way for a vertical move
        if (x1 == x2) {
            start = Math.min(y1, y2) + 1;
            end = Math.max(y1, y2);
            for (i = start; i < end; i++) {
                if (board[x1][i] != null)
                    return true; // There is a piece on the way
            }
        }

        return false; // No pieces on the way
    }

    /**
     * Retrieves the owner of the piece located at the specified position on the game board.
     *
     * @param position The position for which to retrieve the piece owner.
     * @return The owner of the piece located at the specified position, or null if there is no piece at that position.
     */
    public Player getPieceOwner(Position position) {
        Piece piece = getPieceAtPosition(position);
        if (piece != null)
            return piece.getOwner();

        return null;
    }

    /**
     * Moves a concrete piece from one position to another on the game board.
     *
     * @param a The initial position of the piece to be moved.
     * @param b The position where the piece will be moved to.
     * @return The concrete piece that was moved.
     */
    public ConcretePiece movePiece(Position a, Position b) {
        //Extract row and column values for positions a and b
        int xA = a.getRow(), yA = a.getCol(), xB = b.getRow(), yB = b.getCol();
        //Retrieve the concrete piece located at position a
        ConcretePiece movingPiece = board[xA][yA];
        //Move the piece to position b on the game board
        board[b.getRow()][b.getCol()] = movingPiece;

        //If position b was previously null, create a new Position object for it
        if (positions[xB][yB] == null)
            positions[xB][yB] = new Position(xB, yB);

        //Clear the original position of the piece
        board[xA][yA] = null;

        //Return the concrete piece that was moved
        return movingPiece;
    }

    /**
     * Updates the position history after moving a piece on the game board.
     *
     * @param newPosition   The new position of the moved piece.
     * @param movingPiece   The concrete piece that has been moved.
     * @param lastPosition  The previous position of the moved piece.
     */
    public void updatePositionHistory(Position newPosition, ConcretePiece movingPiece, Position lastPosition) {
        //Raise the moved piece at its new position in the position history
        positions[newPosition.getRow()][newPosition.getCol()].raisePiece(movingPiece);
        //If the new position does not already contain the moved piece, add it to the position's piece list
        if (!newPosition.getPieces().contains(movingPiece))
            addStepToPos(newPosition.getRow(), newPosition.getCol());
        //Update the displacement of the moved piece based on its new and last positions
        movingPiece.setDis(movingPiece.getDis() + computeDis(lastPosition, newPosition));
    }

    /**
     * Computes the displacement between two positions on the game board.
     *
     * @param a The first position.
     * @param b The second position.
     * @return The displacement between the two positions.
     */
    public int computeDis(Position a, Position b) {
        int dis = 0;
        //If the rows of both positions are equal, compute the displacement based on column difference
        if(a.getRow() == b.getRow())
            dis = Math.abs(a.getCol() - b.getCol());

        //If the columns of both positions are equal, compute the displacement based on row difference
        else if(a.getCol() == b.getCol())
            dis = Math.abs(a.getRow() - b.getRow());

        return dis;
    }

    /**
     * Increments the number of pieces at a specific position on the game board.
     *
     * @param i The row index of the position.
     * @param j The column index of the position.
     */
    public void addStepToPos(int i, int j) {
        numOfPiece[i][j]++;
    }

    /**
     * Checks for captures and victory conditions after a piece has moved to a destination position.
     *
     * @param destination The position where the piece has been moved.
     */
    public void checkKillAndVictory(Position destination) {
        //Check if the destination is a corner and the king wins
        if (checkCorner(destination) && kingWin()) {
            gameFinished = true;

            return;
        }

        int x = destination.getRow(), y = destination.getCol();
        if (board[x][y] instanceof Pawn) {
            int x1_left = x - 1, x2_left = x - 2, x1_right = x + 1, x2_right = x + 2, y1_up = y - 1, y2_up = y - 2, y1_down = y + 1, y2_down = y + 2;

            //Check for captures in the left direction
            if (x != 0 && (x1_left == 0 || (board[x2_left][y] != null && !(board[x2_left][y] instanceof King) && (board[x2_left][y].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) || checkCorner(new Position(x2_left, y))))) {
                if (board[x1_left][y] != null && board[x1_left][y].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne())
                    handleCapture(x1_left, y, x, y);
            }

            //Check for captures in the right direction
            if (x1_right == board.length - 1 || (x2_right < board.length && board[x2_right][y] != null && !(board[x2_right][y] instanceof King) && board[x2_right][y].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) || checkCorner(new Position(x2_right, y))) {
                if (x1_right < board.length && board[x1_right][y] != null && board[x1_right][y].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) 
                    handleCapture(x1_right, y, x, y);
            }

            //Check for captures in the upward direction
            if (y != 0 && (y1_up == 0 || (board[x][y2_up] != null && !(board[x][y2_up] instanceof King) && board[x][y2_up].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) || checkCorner(new Position(x, y2_up)))) {
                if (board[x][y1_up] != null && board[x][y1_up].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) 
                    handleCapture(x, y1_up, x, y);
            }

            //Check for captures in the downward direction
            if (y1_down == board[0].length - 1 || (y2_down < board[0].length && board[x][y2_down] != null && !(board[x][y2_down] instanceof King) && board[x][y2_down].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) || checkCorner(new Position(x, y2_down))) {
                if (y1_down < board[0].length && board[x][y1_down] != null && board[x][y1_down].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) 
                    handleCapture(x, y1_down, x, y);
            }
        }
    }

    /**
     * Handles the capture of a pawn at the specified position.
     *
     * @param captureX  The row index of the position of the pawn to be captured.
     * @param captureY  The column index of the position of the pawn to be captured.
     * @param currentX  The current row index of the capturing pawn.
     * @param currentY  The current column index of the capturing pawn.
     */
    public void handleCapture(int captureX, int captureY, int currentX, int currentY) {
        //Check if the piece to be captured is a pawn
        if (board[captureX][captureY] instanceof Pawn) {
            //Add the captured pawn to the list of captured pieces
            capturedPieces.add(board[captureX][captureY]);
            //Remove the captured pawn from the board
            board[captureX][captureY] = null; // Capture!
            //Increment the kill count of the capturing pawn (if it's a Pawn type)
            ((Pawn) board[currentX][currentY]).addKill();
        }
    }

    /**
     * Checks if all red pieces have been captured by blue.
     *
     * @return true if all red pieces have been captured by blue, false otherwise.
     */
    public boolean blueEatAllRed() {
        int sum = 0;
        //Iterate over all pieces on the board
        for (ConcretePiece piece : pieces) {
            //Check if the piece belongs to player one (blue)
            if (piece.getOwner() == playerOne && piece instanceof Pawn) {
                //Increment the sum by the number of kills the blue piece has
                sum += ((Pawn) piece).getKills();
            }
        }
        //If the sum of kills of all blue pieces is equal to the total number of red pieces (24), mark the game as finished
        if (sum == 24) {
            gameFinished = true;
            return true;
        }

        return false;
    }

    /**
     * Checks if the king piece is surrounded by opponent pieces.
     *
     * @return true if the king is surrounded by opponent pieces, false otherwise.
     */
    public boolean isKingSurrounded() {
        int i, j, surrounded;
        for (i = 0; i < 11; i++) {
            for (j = 0; j < 11; j++) {
                //Check if the current position has a king piece
                if (board[i][j] != null && board[i][j].getType().equals("♔")) {
                    surrounded = 0;
                    //Check if the king is surrounded by opponent pieces in all four directions
                    if (i == 0 || (board[i - 1][j] != null && board[i - 1][j].getOwner() == playerTwo))
                        surrounded++;

                    if (i == 10 || (board[i + 1][j] != null && board[i + 1][j].getOwner() == playerTwo))
                        surrounded++;

                    if (j == 0 || (board[i][j - 1] != null && board[i][j - 1].getOwner() == playerTwo))
                        surrounded++;

                    if (j == 10 || (board[i][j + 1] != null && board[i][j + 1].getOwner() == playerTwo))
                        surrounded++;

                    //If the king is completely surrounded, mark the game as finished
                    if (surrounded == 4) {
                        gameFinished = true;
                        return true;
                    }

                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the king piece has won the game.
     *
     * @return true if the king has won the game, false otherwise.
     */
    public boolean kingWin() {
        int i, j;

        //Check if the king is present in any of the corner positions
        if (board[0][0] != null && board[0][0].getType().equals("♔")) {
            gameFinished = true;
            return true;
        }

        if (board[0][10] != null && board[0][10].getType().equals("♔")) {
            gameFinished = true;
            return true;
        }

        if (board[10][0] != null && board[10][0].getType().equals("♔")) {
            gameFinished = true;
            return true;
        }

        if (board[10][10] != null && board[10][10].getType().equals("♔")) {
            gameFinished = true;
            return true;
        }

        return true;
    }

    /**
     * Prints the game's win conditions and statistics.
     * It displays the historical movements of the pieces,
     * the number of kills each piece made, the distance each piece moved,
     * and the number of pieces passed through each position.
     */
    public void printWin() {
        int i;
        //1:Historical Movements of Pieces
        pieces.sort(stepHistComper);
        if (winner == playerOne) {
            for (ConcretePiece piece : pieces) {
                if (piece.getOwner() == playerOne) {
                    if (piece.getAllPositions().size() > 1) {
                        String result = toStringHis(piece);
                        if (piece.getType().equals("♔"))
                            System.out.println("K" + piece.getNumber() + ": " + result);
                        else
                            System.out.println("D" + piece.getNumber() + ": " + result);

                    }
                }
            }
            for (ConcretePiece piece : pieces) {
                if (piece.getOwner() == playerTwo) {
                    if(piece.getAllPositions().size() > 1) {
                        String result = toStringHis(piece);
                        System.out.println("A" + piece.getNumber() + ": " + result);
                    }
                }
            }
        }
        if (winner == playerTwo) {
            for (ConcretePiece piece : pieces) {
                if (piece.getOwner() == playerTwo) {
                    if (piece.getAllPositions().size() > 1) {
                        String result = toStringHis(piece);
                        System.out.println("A" + piece.getNumber() + ": " + result);
                    }
                }
            }
            for (ConcretePiece piece : pieces) {
                if (piece.getOwner() == playerOne) {
                    if(piece.getAllPositions().size() > 1) {
                        String result = toStringHis(piece);
                        if (piece.getType().equals("♔"))
                            System.out.println("K" + piece.getNumber() + ": " + result);
                        else
                            System.out.println("D" + piece.getNumber() + ": " + result);
                    }
                }
            }
        }

        rowOfAsterisks();

        //2: Number of Kills Each Piece Made
        pieces.sort(killCountComper); //sorted by the number of kills
        for(i = 0; i < pieces.size(); i++) {
            String ans = "";
            if (pieces.get(i) instanceof Pawn) {
                Pawn p1 = (Pawn) pieces.get(i);
            if (p1.getKills() > 0) {
                if (p1.getOwner() == playerOne) {
                    ans = ans + "D";
                } else if (p1.getOwner() == playerTwo)
                    ans = ans + "A";

                System.out.println(ans + p1.getNumber() + ": " + p1.getKills() + " kills");
            }
        }
        }

        rowOfAsterisks();

        //3: Distance Each Piece Moved
        pieces.sort(distComper);
        for(i=0; i < pieces.size(); i++) {
            String ans = "";
            if(pieces.get(i).getDis() > 0) {
                if(pieces.get(i).getOwner() == playerOne) {
                    if(pieces.get(i).getType().equals("♔"))
                        ans = ans + "K";
                    else
                        ans = ans + "D";
                }
                else if(pieces.get(i).getOwner() == playerTwo)
                    ans = ans + "A";

                System.out.println(ans + pieces.get(i).getNumber() + ": " + pieces.get(i).getDis() + " squares");
            }
        }

        rowOfAsterisks();

        //4: Number of Pieces Passed Through Each Position
        ArrayList<Position> lst = new ArrayList<Position>();
        lst = listNumOfPieces(numOfPiece);
        for (Position position : lst)
            System.out.println(toStringPos(position) + getNumPiecesPassed(position) + " pieces");


        rowOfAsterisks();
    }

    /**
     * Retrieves the number of pieces that have passed through a given position on the board.
     *
     * @param p The position for which to retrieve the number of pieces passed.
     * @return The number of pieces that have passed through the specified position.
     */
    public int getNumPiecesPassed(Position p) {
        return numOfPiece[p.getRow()][p.getCol()];
    }

    /**
     * Builds a list containing all positions that had at least 2 pieces on them throughout the game.
     *
     * @param numOfPieces The 2D array representing the count of pieces passed through each position.
     * @return An ArrayList containing positions that had at least 2 pieces on them during the game.
     */
    public ArrayList<Position> listNumOfPieces(int[][] numOfPieces) {
        ArrayList<Position> lst = new ArrayList<Position>();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Position curr = new Position(i, j);
                if (getNumPiecesPassed(curr) >= 2) {
                    lst.add(curr);
                }
            }
        }
        lst.sort(numOfPiecesComper);
        return lst;
    }

    public String toStringPos(Position p) {
        String ans = "";
        ans = "(" + p.getRow() + ", " + p.getCol() + ")";

        return ans;
    }

    /**
     * Generates a string representation of the historical movements of a concrete piece.
     *
     * @param piece The concrete piece for which to generate the historical movements string.
     * @return A string representation of the historical movements of the concrete piece.
     */
    public String toStringHis(ConcretePiece piece) {
        int i;
        String ans = "["+ toStringPos(piece.getAllPositions().get(0));
        for (i = 1; i < piece.getAllPositions().size(); i++)
            ans = ans + ", " + toStringPos(piece.getAllPositions().get(i));

        ans = ans + "]";

        return ans;
    }

    /**
     * Prints a row of asterisks to the console.
     * The row consists of 74 asterisks.
     */
    public void rowOfAsterisks() {
        int i;
        for(i = 0; i <= 73; i++)
            System.out.print("*");

        System.out.println("*");
    }

    /**
     * Switches the turn between the players.
     * If it's currently the second player's turn, it changes to the first player's turn,
     * and vice versa.
     */
    public void switchTurn() {
        this.secondPlayerTurn = !secondPlayerTurn;
    }

    /**
     * Comparator for comparing positions based on the number of pieces that have passed through them during the game.
     * Positions with a higher number of pieces passed are considered 'smaller'.
     * If two positions have the same number of pieces passed, their row and column indices are compared.
     */
    Comparator<Position> numOfPiecesComper = new Comparator<Position>() {
        public int compare(Position p1, Position p2) {
            int numOfPieces1 = getNumPiecesPassed(p1), numOfPieces2 = getNumPiecesPassed(p2);

            //Compare based on the number of pieces passed through the positions
            if (numOfPieces1 < numOfPieces2)
                return 1; //p1 has fewer pieces passed, so it is considered 'larger'

             else if (numOfPieces1 > numOfPieces2)
                return -1; //p2 has fewer pieces passed, so it is considered 'larger'

                //If the number of pieces passed is equal, compare based on position indices
                //Compare row indices
            else if (numOfPieces1 - numOfPieces2 == 0) { //they are the same - check the position itself
                if (p1.getRow() < p2.getRow())
                    return -1;

                else if (p1.getRow() > p2.getRow())
                    return 1;

                else { //If the row indices are equal, compare column indices
                    if (p1.getCol() < p2.getCol())
                        return -1;

                    else if (p1.getCol() > p2.getCol())
                        return 1;
                }
            }
            return 0; //Positions are considered equal
        }
    };

    /**
     * Comparator for comparing concrete pieces based on their historical movement steps and serial numbers.
     * Concrete pieces with fewer historical movement steps are considered 'smaller'.
     * If two pieces have the same number of historical movement steps, their serial numbers are compared.
     */
    Comparator<ConcretePiece> stepHistComper = new Comparator<ConcretePiece>() {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            // Compare based on the number of historical movement steps
            if (p1.getAllPositions().size() < p2.getAllPositions().size())
                return -1; //p1 has fewer historical movement steps, so it is considered 'smaller'

            else if (p1.getAllPositions().size() > p2.getAllPositions().size())
                return 1; //p2 has fewer historical movement steps, so it is considered 'smaller'

                //If the number of steps is the same, compare based on the serial number of the piece
            else {
                if (p1.getNumber() < p2.getNumber())
                    return -1; //p1 has a smaller serial number, so it is considered 'smaller'

                else if (p1.getNumber() > p2.getNumber())
                    return 1; //p2 has a smaller serial number, so it is considered 'smaller'

            }

            return 0; //Pieces are considered equal
        }
    };

    /**
     * Comparator for comparing concrete pieces based on their number of kills and serial numbers.
     * Concrete pieces with a higher number of kills are considered 'smaller'.
     * If two pieces have the same number of kills, their serial numbers are compared.
     * In case of a tie in kills and serial numbers, the comparator considers the winner and the ownership of the pieces.
     * - If playerOne wins and p1 belongs to playerOne while p2 belongs to playerTwo, p1 is considered 'smaller'.
     * - If playerTwo wins and p1 belongs to playerTwo while p2 belongs to playerOne, p1 is considered 'smaller'.
     */
    Comparator<ConcretePiece> killCountComper = new Comparator<ConcretePiece>() {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            //Compare based on the number of kills
            if (p1 instanceof Pawn && p2 instanceof Pawn) {
                if (((Pawn) p1).getKills() > ((Pawn) p2).getKills())
                    return -1; //p1 has more kills, so it is considered 'smaller'

                else if (((Pawn) p1).getKills() < ((Pawn) p2).getKills())
                    return 1; //p2 has more kills, so it is considered 'smaller'

                else { //If the number of kills is the same, compare based on the serial number of the piece
                    if (p1.getNumber() < p2.getNumber())
                        return -1; //p1 has a smaller serial number, so it is considered 'smaller'

                    else if (p1.getNumber() > p2.getNumber())
                        return 1; //p2 has a smaller serial number, so it is considered 'smaller'

                        //If the serial numbers are the same, consider the winner and ownership of the pieces
                    else if ((winner == playerOne && p1.getOwner() == playerOne && p2.getOwner() == playerTwo) || (winner == playerTwo && p1.getOwner() == playerTwo && p2.getOwner() == playerOne))
                        return 1; //p1 belongs to the winning player or p1 belongs to playerOne and p2 to playerTwo, so p1 is 'smaller'

                    else if ((winner == playerOne && p1.getOwner() == playerTwo && p2.getOwner() == playerOne) || (winner == playerTwo && p1.getOwner() == playerOne && p2.getOwner() == playerTwo))
                        return -1; //p2 belongs to the winning player or p2 belongs to playerOne and p1 to playerTwo, so p1 is 'larger'

                }
            }
            return 0; //Pieces are considered equal

            }
    };

    /**
     * Comparator for comparing concrete pieces based on their distance traveled and serial numbers.
     * Concrete pieces with a greater distance traveled are considered 'smaller'.
     * If two pieces have the same distance traveled, their serial numbers are compared.
     * In case of a tie in distance traveled and serial numbers, the comparator considers the winner and the ownership of the pieces.
     * - If playerOne wins and p1 belongs to playerOne while p2 belongs to playerTwo, p1 is considered 'smaller'.
     * - If playerTwo wins and p1 belongs to playerTwo while p2 belongs to playerOne, p1 is considered 'smaller'.
     */
    Comparator<ConcretePiece> distComper = new Comparator<ConcretePiece>() {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            //Compare based on the distance traveled
            if (p1.getDis() < p2.getDis())
                return 1; //p1 has traveled less distance, so it is considered 'larger'

            else if (p1.getDis() > p2.getDis())
                return -1; //p2 has traveled less distance, so it is considered 'smaller'

            else { //If the distances traveled are the same, compare based on the serial number of the piece
                if (p1.getNumber() < p2.getNumber())
                    return -1; //p1 has a smaller serial number, so it is considered 'smaller'

                else if (p1.getNumber() > p2.getNumber())
                    return 1; //p2 has a smaller serial number, so it is considered 'smaller'

                else { //If the serial numbers are the same, consider the winner and ownership of the pieces
                    if ((winner == playerOne && p1.getOwner() == playerOne && p2.getOwner() == playerTwo) || (winner == playerTwo && p1.getOwner() == playerTwo && p2.getOwner() == playerOne))
                        return 1; //p1 belongs to the winning player or p1 belongs to playerOne and p2 to playerTwo, so p1 is 'smaller'

                    else if ((winner == playerOne && p1.getOwner() == playerTwo && p2.getOwner() == playerOne) || (winner == playerTwo && p1.getOwner() == playerOne && p2.getOwner() == playerTwo))
                        return -1; //p2 belongs to the winning player or p2 belongs to playerOne and p1 to playerTwo, so p1 is 'larger'
                }
            }

            return 0; //Pieces are considered equal
        }
    };



}
