package mustangBot.commands;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.commons.waiter.*;
import mustangBot.events.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.*;
import java.util.concurrent.*;

public class TicTacToeCommand extends Command {

    private EventWaiter waiter;
    private TicTacToe board;
    private Member initiator;
    private Member opponent;
    private char curPlayerToken = TicTacToe.PLAYER1;
    private Member curPlayerName;
    private int rows;
    private int columns;
    private int win;

    public TicTacToeCommand(EventWaiter w){
        this.name = "ttt";
        this.help = "Starts a game of Tic Tac Toe against another server member";
        this.aliases = new String[] {"tplace"};
        this.waiter = w;
    }

    @Override
    protected void execute(CommandEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");

        if(messageSent[0].equalsIgnoreCase("!ttt")){
            initiator = event.getMember();
            curPlayerName = initiator;

            if(messageSent.length == 4) {
                if (Integer.parseInt(messageSent[1]) >= TicTacToe.MIN_NUM_ROWS && Integer.parseInt(messageSent[1]) <= TicTacToe.MAX_NUM_ROWS) {
                    rows = Integer.parseInt(messageSent[1]);
                } else {
                    rows = TicTacToe.MIN_NUM_ROWS;
                }
                if (Integer.parseInt(messageSent[2]) >= TicTacToe.MIN_NUM_COLUMNS && Integer.parseInt(messageSent[2]) <= TicTacToe.MAX_NUM_COLUMNS) {
                    columns = Integer.parseInt(messageSent[2]);
                } else {
                    columns = 3;
                }
                if (Integer.parseInt(messageSent[3]) >= TicTacToe.MIN_WIN && Integer.parseInt(messageSent[3]) <= TicTacToe.MAX_WIN) {
                    win = Integer.parseInt(messageSent[3]);
                } else {
                    win = 3;
                }
            }

            event.reply(initiator.getAsMention() + ", now give me the name of the user to play against. e.g. @MustangBot");
            waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()), e -> {
                try{
                    opponent = e.getMessage().getMentionedMembers().get(0);
                    board = new TicTacToe(rows, columns, win);
                    event.reply(board.toString() + "\nTo place a marker on the board type  \"!tplace <row> <column>\"\n" + curPlayerName.getAsMention() + " (" + curPlayerToken + ") it is your turn!");
                }catch(IndexOutOfBoundsException ex){
                    event.reply("You need to provide the name as a mention");
                }
            }, 30, TimeUnit.SECONDS, () -> event.reply("You did not provide a user to play against fast enough. Try again."));
        }else if(messageSent[0].equalsIgnoreCase("!tplace")){
            if(this.board == null){
                event.reply("Must use the !ttt command to start a Tic Tac Toe game!");
            }else{
                //Check to see if they're a valid user for this object
                if(event.getMember() != initiator && event.getMember() != opponent){
                    event.reply("You're not a part of this game, if you want to start your own game with another player type \"!ttt\"");
                    return;
                }
                //If the person who is placing a marker isn't the current player then tell them to wait their turn and return out of method
                if(event.getMember() != curPlayerName){
                    event.reply("It isn't your turn, your opponent must place a marker first!");
                    return;
                }

                //Parsing the string for the location to place a marker
                int row = Integer.parseInt(messageSent[1]);
                int col = Integer.parseInt(messageSent[2]);

                //Checking to see if the location is a valid spot i.e. it is both empty and a location on the board
                if(!board.checkSpace(row, col)){
                    event.reply("That space is unavailable, pick another location by re-typing  \"!tplace <row> <column>\"");
                }else{
                    //If it is a valid spot place the marker, print out the new board
                    board.placeMarker(row, col, curPlayerToken);
                    event.reply(board.toString());
                    //Check if the most recently placed spot gives a winner
                    if(board.checkForWinner(row, col, curPlayerToken)){
                        event.reply(curPlayerName.getAsMention() + " (" + curPlayerToken + ") wins! To play again type  \"!ttt\"");
                        board = null;
                        //Check if the most recently placed spot results in a draw
                    }else if(board.checkForDraw()){
                        event.reply("Game is a draw! If you want to play again type  \"!ttt\"");
                        board = null;
                        //If isn't a win or a draw swap the current player variables for both token and name
                    }else{
                        swapCurPlayerToken();
                        swapCurPlayerName();
                        event.reply(curPlayerName.getAsMention() + " (" + curPlayerToken + ") it is your turn!");
                    }
                }
            }
        }
    }

    //Method to swap the current player token (X or O)
    private void swapCurPlayerToken(){
        if(curPlayerToken == TicTacToe.PLAYER1){
            curPlayerToken = TicTacToe.PLAYER2;
        }else{
            curPlayerToken = TicTacToe.PLAYER1;
        }
    }

    //Method to swap the name of the person who's turn it is
    private void swapCurPlayerName(){
        if(curPlayerName == initiator){
            curPlayerName = opponent;
        }else{
            curPlayerName = initiator;
        }
    }
}
