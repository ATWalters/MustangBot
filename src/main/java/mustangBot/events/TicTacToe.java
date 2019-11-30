package mustangBot.events;

public class TicTacToe {
    private char[][] board;
    private int win;
    private int rows;
    private int cols;
    private int max_plays;
    private int plays = 0;

    public static final char PLAYER1 = 'X';
    public static final char PLAYER2 = 'O';
    private static final char EMPTY = ' ';
    private static final String SEPARATOR = "|";
    public static final int MIN_NUM_ROWS = 3;
    public static final int MAX_NUM_ROWS = 10;
    public static final int MIN_NUM_COLUMNS = 3;
    public static final int MAX_NUM_COLUMNS = 10;
    public static final int MIN_WIN = 3;
    public static final int MAX_WIN = 5;

    public TicTacToe(int r, int c, int w){
        rows = r;
        cols = c;
        win = w;
        max_plays = r * c;
        board = new char[r][c];
        for(int i = 0; i < r; ++i){
            for(int j = 0; j < c; ++j){
                board[i][j] = EMPTY;
            }
        }
    }

    private int getRows(){
        return rows;
    }

    private int getCols(){
        return cols;
    }

    private int getNumWin(){
        return win;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("```    ");
        for(int i = 0; i < cols; i++){
            if(i <= 10){
                str.append(i).append(" ");
            }else {
                str.append(i).append(" ");
            }
        }
        str.append("\n");
        for(int j = 0; j < rows; j++){
            if(j <= 10) {
                str.append(" ").append(j).append(" ");
            }else{
                str.append(j);
            }
            for(int k = 0; k < cols; k++) {
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

    public boolean checkSpace(int r, int c){
        return board[r][c] == EMPTY && r <= MAX_NUM_ROWS && c <= MAX_NUM_COLUMNS;
    }

    public void placeMarker(int r, int c, char player){
        this.board[r][c] = player;
        this.plays++;
    }

    public boolean checkForDraw(){
        return plays == max_plays;
    }

    public boolean checkForWinner(int r, int c, char player){
        return checkHorizontalWin(r, player) || checkVerticalWin(c, player) || checkDiagonalWin(player);
    }

    private boolean checkHorizontalWin(int r,char player){
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

    private boolean checkVerticalWin(int c, char player){
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

    private boolean checkDiagonalWin(char player){
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
