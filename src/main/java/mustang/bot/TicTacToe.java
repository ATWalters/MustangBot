package mustang.bot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicTacToe extends ListenerAdapter {

    private char[][] board;
    private char curPlayer;
    private int plays;
    private static final int NUM_TO_WIN = 3;
    private static final int SIZE = 3;
    private static final char PLAYER1 = 'X';
    private static final char PLAYER2 = 'O';
    private static final char EMPTY = ' ';
    private static final int MAX_PLAYS = SIZE * SIZE;
    private static final String SEPARATOR = "|";

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        String user = event.getMember().getAsMention();

        if(event.getAuthor().isBot()){
            return;
        }

        if(messageSent[0].equalsIgnoreCase("!ttt")){
            curPlayer = PLAYER1;
            plays = 0;
            this.board = new char[SIZE][SIZE];
            for(int i = 0; i < SIZE; i++){
                for(int j = 0; j < SIZE; j++){
                    this.board[i][j] = EMPTY;
                }
            }
            event.getChannel().sendMessage("To place a token on the board type   \"!tplace <row> <col>\"   " +
                    "replacing the row and column with the location to place your marker " + user).queue();
            event.getChannel().sendMessage(toString()).queue();
            event.getChannel().sendMessage("Player " + curPlayer + " it is your turn!").queue();
        }

        if(messageSent[0].equalsIgnoreCase("!tplace")){
            int row = Integer.parseInt(messageSent[1]);
            int col = Integer.parseInt(messageSent[2]);
            if(!checkSpace(row, col)){
                event.getChannel().sendMessage("That space is unavailable, pick another location by re-typing  \"!tplace <row> <col>").queue();
            }else{
                this.placeMarker(row, col, curPlayer);
                event.getChannel().sendMessage(toString()).queue();
                if (this.checkForDraw()) {
                    event.getChannel().sendMessage("Game is a draw! If you want to play again type  \"!ttt").queue();
                } else if (this.checkForWinner(row, col, curPlayer)) {
                    event.getChannel().sendMessage("Player " + curPlayer + " wins! If you want to play again type  \"!ttt").queue();
                }
                this.swapPlayer();
                event.getChannel().sendMessage("Player " + curPlayer + " it is your turn!").queue();
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("```   ");
        for(int i = 0; i < SIZE; i++){
            str.append(i).append(" ");
        }
        str.append("\n");
        for(int j = 0; j < SIZE; j++){
            str.append(j).append(" ");
            for(int k = 0; k < SIZE; k++) {
                str.append(SEPARATOR).append(whatsAtPos(j, k));
            }
            str.append(SEPARATOR).append("\n");
        }
        str.append("```");
        return str.toString();
    }

    private char whatsAtPos(int r, int c){
        return board[r][c];
    }

    private boolean checkSpace(int r, int c){
        return this.board[r][c] == EMPTY;
    }

    private void placeMarker(int r, int c, char player){
        this.board[r][c] = player;
        this.plays++;
    }

    private void swapPlayer(){
        if(curPlayer == PLAYER1){
            curPlayer = PLAYER2;
        }else{
            curPlayer = PLAYER1;
        }
    }

    private boolean checkForDraw(){
        return plays == MAX_PLAYS;
    }

    private boolean checkForWinner(int r, int c, char player){
        return checkHorizontalWin(r, c, player) || checkVerticalWin(r, c, player) || checkDiagonalWin(r, c, player);
    }

    private boolean checkHorizontalWin(int r, int c, char player){
        int inARow = 0;

        for(int i = 0; i < SIZE; i++){
            if(whatsAtPos(r,i) == player){
                inARow++;
                if(inARow == NUM_TO_WIN){
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

        for(int i = 0; i < SIZE; i++){
            if(whatsAtPos(i,c) == player) {
                inARow++;
                if (inARow == NUM_TO_WIN) {
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

        for(col = 0; col < (SIZE - (NUM_TO_WIN - 1)); col++){
            for(row = 0; row < (SIZE - (NUM_TO_WIN - 1)); row++){
                while(whatsAtPos(row, col) == player){
                    row++;
                    col++;
                    inARow++;
                    if(inARow == NUM_TO_WIN){
                        return true;
                    }
                }
                inARow = 0;
            }
        }

        for(col = SIZE - 1; col > NUM_TO_WIN - 2; col--){
            for(row = 0; row < (SIZE - (NUM_TO_WIN - 1)); row++){
                while(whatsAtPos(row, col) == player){
                    row++;
                    col--;
                    inARow++;
                    if(inARow == NUM_TO_WIN){
                        return true;
                    }
                }
                inARow = 0;
            }
        }
        return false;
    }
}
