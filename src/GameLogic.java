import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private ConcretePlayer playerOne;
    private ConcretePlayer playerTwo;
    private ConcretePiece[][] board;
    private Position[][] positions;
    private boolean secondPlayerTurn;
    private boolean gameFinished;
    private static int BOARD_SIZE = 11;
    private List<ConcretePiece> capturedPieces;
    private final List<ConcretePiece> pieces = new ArrayList<>();
    private Stack<Move> moveHistory;
    private record Move(Position to, List<ConcretePiece> capturedPieces) {}
    private ConcretePlayer winner;

    public GameLogic() {
        this.positions = new Position[BOARD_SIZE][BOARD_SIZE];
        this.secondPlayerTurn = true;
        this.playerOne = new ConcretePlayer(true);
        this.playerTwo = new ConcretePlayer(false);
        this.moveHistory = new Stack<>();
        reset();
    }
    @Override
    public boolean move(Position a, Position b) {
        boolean playerTwoTurn = isSecondPlayerTurn();
        boolean playerOnePosition = getPieceAtPosition(a).getOwner().isPlayerOne();

        if (isInvalidMove(a, b) || (playerTwoTurn && playerOnePosition) || (!playerTwoTurn && !playerOnePosition))
            return false;

        capturedPieces = new ArrayList<>();

        ConcretePiece movingPiece = movePiece(a, b);

        updatePositionHistory(b, movingPiece);

        checkKillAndVictory(b);

        switchTurn();

        addToMoveHistory(b, capturedPieces);

        return true;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        return board[position.getRow()][position.getCol()];
    }

    @Override
    public Player getFirstPlayer() {
        return playerOne;
    }

    @Override
    public Player getSecondPlayer() {
        return playerTwo;
    }

    @Override
    public boolean isGameFinished() {
        return gameFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return secondPlayerTurn;
    }

    @Override
    public void reset() {
        int i, j;
        gameFinished = false;
        secondPlayerTurn = true;
        board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        //now we will create and place ich pawn first player one's pawns and than for player two's pawns.
        for(i = 4; i <= 6; i++) {
            for(j = 4; j <= 6; j++) {
                if( i !=5 || j !=5)
                    board[i][j] = new Pawn(playerOne);
                else
                    board[i][j] = new King(playerOne);
            }
        }
        board[5][3] = new Pawn(playerOne);
        board[3][5] = new Pawn(playerOne);
        board[5][7] = new Pawn(playerOne);
        board[7][5] = new Pawn(playerOne);

        //player two
        for(i = 3; i<=7; i++)
            board[0][i] = new Pawn(playerTwo);

        for(i = 3; i <= 7; i++)
            board[i][0] = new Pawn(playerTwo);

        for(i = 3; i <= 7; i++)
            board[10][i] = new Pawn(playerTwo);

        for(i = 3; i <= 7; i++)
            board[i][10] = new Pawn(playerTwo);

        board[1][5] = new Pawn(playerTwo);
        board[5][1] = new Pawn(playerTwo);
        board[9][5] = new Pawn(playerTwo);
        board[5][9] = new Pawn(playerTwo);



    }

    @Override
    public void undoLastMove() {

    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public boolean checkCorner(Position position) {
        return (position.getRow() % 10 == 0 && position.getCol() % 10 == 0);
    }

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

    public boolean isVerticalOrHorizontalMove(Position source, Position destination) {
        return source.getRow() == destination.getRow() || source.getCol() == destination.getCol();
    }

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
                if (board[i][y1] != null) {
                    return true; // There is a piece on the way
                }
            }
        }

        // Check if there are pieces on the way for a vertical move
        if (x1 == x2) {
            start = Math.min(y1, y2) + 1;
            end = Math.max(y1, y2);
            for (i = start; i < end; i++) {
                if (board[x1][i] != null) {
                    return true; // There is a piece on the way
                }
            }
        }

        return false; // No pieces on the way
    }

    public Player getPieceOwner(Position position) {
        Piece piece = getPieceAtPosition(position);
        if (piece != null)
            return piece.getOwner();

        return null;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }

    public ConcretePiece movePiece(Position a, Position b) {
        int x = a.getRow(), y = a.getCol();
        ConcretePiece movingPiece = board[x][y];

        board[b.getRow()][b.getCol()] = movingPiece;
        movingPiece.updatePosition(b);

        int xB = b.getRow(), yB = b.getCol();
        if (positions[xB][yB] == null)
            positions[xB][yB] = new Position(xB, yB);

        positions[xB][yB].raisePiece(movingPiece);
        board[x][y] = null;

        return movingPiece;
    }

    private void updatePositionHistory(Position newPosition, ConcretePiece movingPiece) {
        positions[newPosition.getRow()][newPosition.getCol()].raisePiece(movingPiece);
    }



    public void checkKillAndVictory(Position destination) {
        if (checkCorner(destination)) {
            if (kingWin())
                playerOne.win();

            return;
        }

        int x = destination.getRow(), y = destination.getCol();
        if (board[x][y] instanceof Pawn) {
            int xLeft1 = x - 1, xLeft2 = x - 2;

            if (x != 0 && (xLeft1 == 0 ||
                    (board[xLeft2][y] != null && !(board[xLeft2][y] instanceof King) &&
                            (board[xLeft2][y].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) ||
                            checkCorner(new Position(xLeft2, y))))) {
                if (board[xLeft1][y] != null && board[xLeft1][y].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) {
                    handleCapture(xLeft1, y, x, y);
                }
            }

            int xRight1 = x + 1, xRight2 = x + 2;
            if (xRight1 == board.length - 1 ||
                    (xRight2 < board.length && board[xRight2][y] != null &&
                            !(board[xRight2][y] instanceof King) &&
                            board[xRight2][y].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) ||
                    checkCorner(new Position(xRight2, y))) {
                if (xRight1 < board.length && board[xRight1][y] != null &&
                        board[xRight1][y].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) {
                    handleCapture(xRight1, y, x, y);
                }
            }

            int yUp1 = y - 1, yUp2 = y - 2;
            if (y != 0 && (yUp1 == 0 ||
                    (board[x][yUp2] != null && !(board[x][yUp2] instanceof King) &&
                            board[x][yUp2].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) ||
                    checkCorner(new Position(x, yUp2)))) {
                if (board[x][yUp1] != null &&
                        board[x][yUp1].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) {
                    handleCapture(x, yUp1, x, y);
                }
            }

            int yDown1 = y + 1, yDown2 = y + 2;
            if (yDown1 == board[0].length - 1 ||
                    (yDown2 < board[0].length && board[x][yDown2] != null &&
                            !(board[x][yDown2] instanceof King) &&
                            board[x][yDown2].getOwner().isPlayerOne() == board[x][y].getOwner().isPlayerOne()) ||
                    checkCorner(new Position(x, yDown2))) {
                if (yDown1 < board[0].length && board[x][yDown1] != null &&
                        board[x][yDown1].getOwner().isPlayerOne() != board[x][y].getOwner().isPlayerOne()) {
                    handleCapture(x, yDown1, x, y);
                }
            }
        }
    }

    public void handleCapture(int captureX, int captureY, int currentX, int currentY) {
        if (board[captureX][captureY] instanceof Pawn) {
            capturedPieces.add(board[captureX][captureY]);
            board[captureX][captureY] = null; // Capture!
            ((Pawn) board[currentX][currentY]).addKill();
        }
        else {
            if (isKingSurrounded()) {
                winner = playerTwo;
                winner.win();
                //declareVictoryAndPrintStats(false);
            }
            else {
                if (kingWin()) {
                    winner = playerOne;
                    winner.win();
                    //declareVictoryAndPrintStats(true);
                }
            }
        }
    }

/*
    public void declareVictoryAndPrintStats(boolean isPlayerOneWon) {
        gameFinished = true;
        ConcretePlayer winner = isPlayerOneWon ? playerOne : playerTwo;
        winner.win();

        // First part
        pieces.sort((piece1, piece2) -> {
            int defenderComparison = Boolean.compare(piece1.getOwner().isPlayerOne(), piece2.getOwner().isPlayerOne());

            if (defenderComparison != 0) {
                return isPlayerOneWon ? -defenderComparison : defenderComparison;
            }

            int stepsComparison = Integer.compare(piece1.getAllPositions().size(), piece2.getAllPositions().size());

            if (stepsComparison != 0) {
                return stepsComparison;
            }

            return Integer.compare(piece1.getNumber(), piece2.getNumber());
        });


        printStats(pieces, (o) -> o.getPositionHistory().size() > 1, ConcretePiece::getPositionHistory);

        // Second Part
        List<Pawn> pawns = pieces.stream()
                .filter(piece -> piece instanceof Pawn)
                .map(piece -> (Pawn) piece)
                .sorted((piece1, piece2) -> {
                    int killsC = Integer.compare(piece2.getKills(), piece1.getKills());
                    return compareByOrder(isDefenderWon, piece1, piece2, killsC);
                })
                .collect(Collectors.toList());

        printStats(pawns, (o) -> o.getKills() > 0, Pawn::getKillsStr);

        // Third part
        pieces.sort((piece1, piece2) -> {
            int ditComp = Integer.compare(piece2.getDist(), piece1.getDist());
            return compareByOrder(isDefenderWon, piece1, piece2, ditComp);
        });
        printStats(pieces, (o) -> o.getDist() > 0, ConcretePiece::getDistStr);

        // Forth part
        List<Position> positionList = Stream.of(positions)
                .flatMap(Stream::of) // Flatten the 2D array into a single stream
                .filter(Objects::nonNull) // Filter out null positions
                .sorted((position1, position2) -> {
                    int ditComp = Integer.compare(position2.getPieceSize(), position1.getPieceSize());
                    if (ditComp != 0)
                        return ditComp;

                    int xComp = Integer.compare(position1.getX(), position2.getX());
                    if (xComp != 0)
                        return xComp;

                    return Integer.compare(position1.getY(), position2.getY());
                })
                .toList();

        printStats(positionList, (position -> position.getPieceSize() > 1), Position::piecesStr);

        pieces.clear();
        positions = new Position[BOARD_SIZE][BOARD_SIZE];
    }

 */

    public void declareVictoryAndPrintStats(boolean isPlayerOneWon) {
        // Set the game as finished
        gameFinished = true;

        // Determine the winner
        ConcretePlayer winner;
        if (isPlayerOneWon)
            winner = playerOne;
        else
            winner = playerTwo;

        winner.win();

        // Sort pieces based on certain criteria
        sortPieces();

        // Print statistics about pieces
        printPieceStats();

        // Clear game state
        clearGameState();
    }

    public void sortPieces() {
        pieces.sort(new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece piece1, ConcretePiece piece2) {
                int ownerComparison = Boolean.compare(piece1.getOwner().isPlayerOne(), piece2.getOwner().isPlayerOne());
                if (ownerComparison != 0)
                    return ownerComparison;


                int stepsComparison = Integer.compare(piece1.getAllPositions().size(), piece2.getAllPositions().size());
                if (stepsComparison != 0)
                    return stepsComparison;


                return Integer.compare(piece1.getNumber(), piece2.getNumber());
            }
        });
    }

    public void printPieceStats() {
        // Print statistics about pieces
        for (ConcretePiece piece : pieces) {
            if (piece.getAllPositions().size() > 1)
                System.out.println(piece.getAllPositions());
        }

        // Filter pawns and print their kill statistics
        List<Pawn> pawns = new ArrayList<>();
        for (ConcretePiece piece : pieces) {
            if (piece instanceof Pawn)
                pawns.add((Pawn) piece);
        }

        pawns.sort(new Comparator<Pawn>() {
            @Override
            public int compare(Pawn pawn1, Pawn pawn2) {
                return Integer.compare(pawn2.getKills(), pawn1.getKills());
            }
        });
        for (Pawn pawn : pawns) {
            if (pawn.getKills() > 0)
                System.out.println(pawn.getKills());
        }

        // Sort and print statistics about piece distance
        pieces.sort(new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece piece1, ConcretePiece piece2) {
                return Integer.compare(piece2.getDis(), piece1.getDis());
            }
        });

        for (ConcretePiece piece : pieces) {
            if (piece.getDis() > 0)
                System.out.println(piece.getDis());
        }

        // Flatten and sort the position array and print statistics
        List<Position> positionList = new ArrayList<>();
        for (Position[] positionArray : positions) {
            for (Position position : positionArray) {
                if (position != null)
                    positionList.add(position);
            }
        }
        positionList.sort(new Comparator<Position>() {
            @Override
            public int compare(Position position1, Position position2) {
                int sizeComparison = Integer.compare(position2.getPieceSize(), position1.getPieceSize());
                if (sizeComparison != 0)
                    return sizeComparison;


                int xComparison = Integer.compare(position1.getRow(), position2.getRow());
                if (xComparison != 0)
                    return xComparison;


                return Integer.compare(position1.getCol(), position2.getCol());
            }
        });

        for (Position position : positionList) {
            if (position.getPieceSize() > 1) {
                System.out.println(position.piecesStr());
            }
        }
    }

    private void clearGameState() {
        pieces.clear();
        positions = new Position[BOARD_SIZE][BOARD_SIZE];
    }
    public boolean isKingSurrounded() {
        int i, j, surrounded;
        for (i = 0; i < 11; i++) {
            for (j = 0; j < 11; j++) {
                if (board[i][j] != null && board[i][j].getType().equals("♔")) {
                    surrounded = 0;
                    if (i == 0 || (board[i - 1][j] != null && board[i - 1][j].getOwner() == playerTwo))
                        surrounded++;

                    if (i == 10 || (board[i + 1][j] != null && board[i + 1][j].getOwner() == playerTwo))
                        surrounded++;

                    if (j == 0 || (board[i][j - 1] != null && board[i][j - 1].getOwner() == playerTwo))
                        surrounded++;

                    if (j == 10 || (board[i][j + 1] != null && board[i][j + 1].getOwner() == playerTwo))
                        surrounded++;

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

    public boolean kingWin() {
        int i, j;

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

        for(i = 0; i < BOARD_SIZE ;i++) {
            for(j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] != null && board[i][j].owner == playerTwo)
                    return false;
            }
        }
        return true;
    }

    public void printWin(){
        int i;
        /////////////סעיף 1//////////
        ArrayList<ConcretePiece> moreT1 = new ArrayList<ConcretePiece>();
        //moreT1 = pHistory(aPiece);
        pieces.sort(stepHistComp);
        if(winner == playerOne) {
            for (ConcretePiece piece : pieces) {
                if (piece.getOwner() == playerOne) {
                    if(piece.getAllPositions().size() > 1) {
                        String result = toStringHis(piece);
                        if (piece.getType() == "♔")
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
        if (winner == playerTwo){
            for (ConcretePiece piece : pieces) {
                if (piece.getOwner() == playerTwo) {
                    if(piece.getAllPositions().size() > 1) {
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
        /////////////סעיף 2//////////
        pieces.sort(killCountComp); //sorted by the number of kills
        for(i = 0; i < pieces.size(); i++) {
            String ans = "";
            if (pieces.get(i).getKills() > 0) {
                if(pieces.get(i).getOwner() == playerOne) {
                    if(pieces.get(i).getType().equals("♔"))
                        ans = ans + "K";

                    else
                        ans= ans + "D";
                }
                else if(pieces.get(i).getOwner() == playerTwo)
                    ans = ans + "A";

                System.out.println(ans + pieces.get(i).getNumber() + ": " + pieces.get(i).getKills() + " kills");
            }
        }

        rowOfAsterisks();
        /////////////סעיף 3//////////
        pieces.sort(distComp);
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
        /////////////סעיף 4//////////
        ArrayList<Position> moreT2 = new ArrayList<Position>();
        moreT2 = listNumOfPieces(numOfPiece);
        for (Position position : moreT2) {
            System.out.println(toStringPos(position) + getNumPiecesPassed(position) + " pieces");
        }
        stars();
    }

    public String toStringPos(Position p) {
        String ans = "";
        ans = "(" + p.getX() + ", " + p.getY() + ")";
        return ans;
    }
    public String toStringHis(ConcretePiece piece) {
        String ans = "["+ toStringPos(piece.pHist.get(0));
        for(int i=1;i<piece.pHist.size();i++){

            ans=ans +", "+ toStringPos(piece.pHist.get(i));
        }
        ans=ans+"]";
        return ans;
    }

    public void rowOfAsterisks() {
        int i;
        for(i = 0; i <= 73; i++){
            System.out.print("*");
        }
        System.out.println("*");
    }

    public void switchTurn() {
        this.secondPlayerTurn = !secondPlayerTurn;
    }

    public void addToMoveHistory(Position b, List<ConcretePiece> capturedPieces) {
        moveHistory.push(new Move(b, capturedPieces));
    }



    public Player getCurrentPlayer() {
        return isSecondPlayerTurn() ? playerTwo: playerOne;
    }

    Comparator<Position> numOfPiecesComp = new Comparator<Position>() {
        public int compare(Position p1, Position p2) {
            if (getNumPiecesPassed(p1) < getNumPiecesPassed(p2)) {
                return 1; //p1 is bigger
            } else if (getNumPiecesPassed(p1) > getNumPiecesPassed(p2)) {
                return -1; //p2 is bigger
            } else if (getNumPiecesPassed(p1) - getNumPiecesPassed(p2) == 0) { //they are the same - check the position itself
                if (p1.getX() < p2.getX()) {
                    return -1;
                } else if (p1.getX() > p2.getX()) {
                    return 1;
                } else { //if the X are equal
                    if (p1.getY() < p2.getY()) {
                        return -1;
                    } else if (p1.getY() > p2.getY()) {
                        return 1;
                    }
                }
            }
            return 0;
        }
    };
    Comparator<ConcretePiece> stepHistComp = new Comparator<ConcretePiece>() {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            //compares according to the number of steps taken by the piece
            if (p1.getAllPositions().size() < p2.getAllPositions().size())
                return -1;

            else if (p1.getAllPositions().size() > p2.getAllPositions().size())
                return 1;

            //if the number of steps is the same compares by the serial number of the piece- smaller goes before
            else {
                if (p1.getNumber() < p2.getNumber())
                    return -1;

                else if (p1.getNumber() > p2.getNumber())
                    return 1;

            }

            return 0;
        }
    };

    Comparator<ConcretePiece> killCountComp = new Comparator<ConcretePiece>() {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            if (p1.getKills() > p2.getKills())
                return -1;

            else if (p1.getKills() < p2.getKills())
                return 1;

            else{
                if (p1.getNumber() < p2.getNumber())
                    return -1;

                else if (p1.getNumber() > p2.getNumber())
                    return 1;

                else
                    if ((winner == playerOne && p1.getOwner() == playerOne && p2.getOwner() == playerTwo) || (winner == playerTwo && p1.getOwner() == playerTwo && p2.getOwner() == playerOne))
                        return 1;

                    else if ((winner == playerOne && p1.getOwner() == playerTwo && p2.getOwner() == playerOne) || (winner == playerTwo && p1.getOwner() == playerOne && p2.getOwner() == playerTwo))
                        return -1;

                }


            return 0;
        }
    };
    Comparator<ConcretePiece> distComp = new Comparator<ConcretePiece>() {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            if (p1.getDis() < p2.getDis())
                return 1;

            else if (p1.getDis() > p2.getDis())
                return -1;

            else{
                if (p1.getNumber() < p2.getNumber())
                    return -1;

                else if (p1.getNumber() > p2.getNumber())
                    return 1;

                else{
                    if ((winner == playerOne && p1.getOwner() == playerOne && p2.getOwner() == playerTwo) || (winner == playerTwo && p1.getOwner() == playerTwo && p2.getOwner() == playerOne))
                        return 1;

                    else if ((winner == playerOne && p1.getOwner() == playerTwo && p2.getOwner() == playerOne) || (winner == playerTwo && p1.getOwner() == playerOne && p2.getOwner() == playerTwo))
                        return -1;

                }
            }

            return 0;
        }
    };




}
