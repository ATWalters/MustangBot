package mustangBot.events;

public class TicTacToe {
    private char[][] board;
    private int NUM_TO_WIN;
    private int ROWS;
    private int COLS;
    private int MAX_PLAYS;
    private int plays = 0;

    static final char PLAYER1 = 'X';
    static final char PLAYER2 = 'O';
    private static final char EMPTY = ' ';
    private static final String SEPARATOR = "|";

    TicTacToe(int r, int c, int w){
        ROWS = r;
        COLS = c;
        NUM_TO_WIN = w;
        MAX_PLAYS = r * c;
        board = new char[r][c];
        for(int i = 0; i < r; ++i){
            for(int j = 0; j < c; ++j){
                board[i][j] = EMPTY;
            }
        }
    }

    private int getRows(){
        return ROWS;
    }

    private int getCols(){
        return COLS;
    }

    private int getNumWin(){
        return NUM_TO_WIN;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("```   ");
        for(int i = 0; i < COLS; i++){
            str.append(i).append(" ");
        }
        str.append("\n");
        for(int j = 0; j < ROWS; j++){
            str.append(j).append(" ");
            for(int k = 0; k < COLS; k++) {
                str.append(TicTacToe.SEPARATOR).append(whatsAtPos(j, k));
            }
            str.append(TicTacToe.SEPARATOR).append("\n");
        }
        str.append("```");
        return str.toString();
    }

    private char whatsAtPos(int r, int c){
        return board[r][c];
    }

    boolean checkSpace(int r, int c){
        return board[r][c] == EMPTY;
    }

    void placeMarker(int r, int c, char player){
        this.board[r][c] = player;
        this.plays++;
    }

    boolean checkForDraw(){
        return plays == MAX_PLAYS;
    }

    boolean checkForWinner(int r, int c, char player){
        return checkHorizontalWin(r, c, player) || checkVerticalWin(r, c, player) || checkDiagonalWin(r, c, player);
    }

    private boolean checkHorizontalWin(int r, int c, char player){
        int inARow = 0;

        for(int i = 0; i < this.getCols(); i++){
            if(whatsAtPos(r,i) == player){
                inARow++;
                if(inARow == this.getNumWin()){
                    return true;
                }
            }else if(whatsAtPos(r,i) != player){
                inARow = 0;
            }
        }
        return false;
    }

    private boolean checkVerticalWin(int r, int c, char player){
        int inARow = 0;

        for(int i = 0; i < this.getRows(); i++){
            if(whatsAtPos(i,c) == player) {
                inARow++;
                if (inARow == this.getNumWin()) {
                    return true;
                }
            }else if(whatsAtPos(i,c) != player){
                inARow = 0;
            }
        }
        return false;
    }

    private boolean checkDiagonalWin(int r, int c, char player){
        int row;
        int col;
        int inARow = 0;

        for(col = 0; col < (this.getCols() - (this.getNumWin() - 1)); col++){
            for(row = 0; row < (this.getRows() - (this.getNumWin() - 1)); row++){
                while(whatsAtPos(row, col) == player){
                    row++;
                    col++;
                    inARow++;
                    if(inARow == this.getNumWin()){
                        return true;
                    }
                }
                inARow = 0;
            }
        }

        for(col = this.getCols() - 1; col > this.getNumWin() - 2; col--){
            for(row = 0; row < (this.getRows() - (this.getNumWin() - 1)); row++){
                while(whatsAtPos(row, col) == player){
                    row++;
                    col--;
                    inARow++;
                    if(inARow == this.getNumWin()){
                        return true;
                    }
                }
                inARow = 0;
            }
        }
        return false;
    }
}
