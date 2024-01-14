public class GameLogic implements PlayableLogic {

    private ConcretePlayer playerOne;
    private ConcretePlayer playerTwo;
    private ConcretePiece[][] board;
    private Position[][] positions;
    private boolean secondPlayerTurn;
    private boolean gameFinished;
    private static int BOARD_SIZE = 11;

    public GameLogic() {
        this.positions = new Position[BOARD_SIZE][BOARD_SIZE];
        this.secondPlayerTurn = true;
        this.playerOne = new ConcretePlayer(true);
        this.playerTwo = new ConcretePlayer(false);
        reset();
    }
    @Override
    public boolean move(Position a, Position b) {
        return false;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        return board[position.getCol()][position.getRow()];
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
        return (position.getCol() % 10 == 0 && position.getRow() % 10 == 0);
    }
}
