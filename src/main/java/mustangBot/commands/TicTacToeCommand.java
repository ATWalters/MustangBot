package mustangBot.commands;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.commons.waiter.*;
import mustangBot.logic.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.*;
import java.awt.*;
import java.util.concurrent.*;

public class TicTacToeCommand extends Command {

    private EventWaiter waiter;
    private TicTacToeLogic board;
    private Member initiator;
    private Member opponent;
    private char curPlayerToken = TicTacToeLogic.PLAYER1;
    private Member curPlayerName;
    private int rows;
    private int columns;
    private int win;
    private EmbedBuilder eb;

    public TicTacToeCommand(EventWaiter w){
        this.name = "ttt";
        this.help = "\nStarts a game of TicTacToe with another member.\nRow and column arguments must be between" +
                " " + TicTacToeLogic.MIN_NUM_COLUMNS + " & " + TicTacToeLogic.MAX_NUM_COLUMNS + " and number to win between " + TicTacToeLogic.MIN_WIN +" & " + TicTacToeLogic.MAX_WIN;
        this.arguments = "[number of rows(optional)] [column(optional)] [number in a row to win(optional)]";
        this.guildOnly = true;
        this.waiter = w;
        this.children = new Command[]{
                new QuitCommand(),
                new PlaceCommand()
        };
    }
    @Override
    protected void execute(CommandEvent event){
        String[] messageSent = event.getArgs().split(" ");

        initiator = event.getMember();
        curPlayerName = initiator;
        eb = new EmbedBuilder();

        if(messageSent.length == 3) {
            if (Integer.parseInt(messageSent[0]) >= TicTacToeLogic.MIN_NUM_ROWS && Integer.parseInt(messageSent[0]) <= TicTacToeLogic.MAX_NUM_ROWS) {
                rows = Integer.parseInt(messageSent[0]);
            } else {
                rows = TicTacToeLogic.MIN_NUM_ROWS;
            }
            if (Integer.parseInt(messageSent[1]) >= TicTacToeLogic.MIN_NUM_COLUMNS && Integer.parseInt(messageSent[1]) <= TicTacToeLogic.MAX_NUM_COLUMNS) {
                columns = Integer.parseInt(messageSent[1]);
            } else {
                columns = 3;
            }
            if (Integer.parseInt(messageSent[2]) >= TicTacToeLogic.MIN_WIN && Integer.parseInt(messageSent[2]) <= TicTacToeLogic.MAX_WIN) {
                win = Integer.parseInt(messageSent[2]);
            } else {
                win = 3;
            }
        }else{
            rows = columns = win = 3;
        }
        event.reply(initiator.getAsMention() + ", now give me the name of the user to play against. e.g. @MustangBot");
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()), e -> {
            try{
                opponent = e.getMessage().getMentionedMembers().get(0);
                board = new TicTacToeLogic(rows, columns, win);
                eb.setTitle("Tic Tac Toe Game Between \n" + initiator.getEffectiveName() + " & " + opponent.getEffectiveName(), "https://www.youtube.com/watch?v=USEjXNCTvcc");
                eb.setColor(Color.CYAN);
                eb.addField("Game Board", board.toString(), true);
                eb.addField("Instructions", "To place a marker on the board type:  \n\"!ttt place <row> <column>\"\n", false);
                event.reply(eb.build());
                event.reply(curPlayerName.getAsMention() + " (" + curPlayerToken + ") it is your turn!");
            }catch(IndexOutOfBoundsException ex){
                event.reply("You need to provide the name as a mention");
            }
            }, 30, TimeUnit.SECONDS, () -> event.reply("You did not provide a user to play against fast enough. Try again."));
    }

    public class PlaceCommand extends Command{

        public PlaceCommand(){
            this.name = "place";
            this.help = "Places a marker on the Tic Tac Toe board";
            this.aliases = new String[] {"tplace"};
            this.guildOnly = true;
        }

        @Override
        protected void execute(CommandEvent event){
            String[] messageSent = event.getArgs().split(" ");

            if(board == null){
                event.reply("Must use the !ttt command to start a Tic Tac Toe game first!");
            }else{
                //Check to see if they're a valid user for this object
                if(event.getMember() != initiator && event.getMember() != opponent){
                    event.reply("You're not a part of this game, if you want to start your own game with another player use \"!ttt\"");
                    return;
                }
                //If the person who is placing a marker isn't the current player then tell them to wait their turn and return out of method
                if(event.getMember() != curPlayerName){
                    event.reply("It isn't your turn, your opponent must place a marker first!");
                    return;
                }

                //Parsing the string for the location to place a marker
                int row = Integer.parseInt(messageSent[0]);
                int col = Integer.parseInt(messageSent[1]);

                //Checking to see if the location is a valid spot i.e. it is both empty and a location on the board
                if(!board.checkSpace(row - 1, col - 1)){
                    event.reply("That space is unavailable, pick another location by re-typing  \"!ttt place <row> <column>\"");
                }else{
                    //If it is a valid spot place the marker, print out the new board
                    board.placeMarker(row - 1, col - 1, curPlayerToken);
                    eb.clearFields();
                    eb.addField("Game Board", board.toString(), true);
                    eb.addField("Instructions", "To place a marker on the board type:  \n\"!ttt place <row> <column>\"\n", false);
                    event.reply(eb.build());
                    //Check if the most recently placed spot gives a winner
                    if(board.checkForWinner(row - 1, col - 1, curPlayerToken)){
                        event.reply(curPlayerName.getAsMention() + " (" + curPlayerToken + ") wins! To play again use  \"!ttt\"");
                        board = null;
                        //Check if the most recently placed spot results in a draw
                    }else if(board.checkForDraw()){
                        event.reply("Game is a draw! If you want to play again use  \"!ttt\"");
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
    public class QuitCommand extends Command{

        public QuitCommand(){
            this.name = "quit";
            this.aliases = new String[] {"tquit"};
            this.help = "Quits an ongoing Tic Tac Toe game";
            this.guildOnly = true;
        }

        @Override
        protected void execute(CommandEvent event){
            if(board == null){
                event.reply("No game was ongoing!");
            }else{
                board = null;
                event.reply(event.getMember().getAsMention() + " has quit the game!");
            }
        }
    }

    //Method to swap the current player token (X or O)
    private void swapCurPlayerToken(){
        if(curPlayerToken == TicTacToeLogic.PLAYER1){
            curPlayerToken = TicTacToeLogic.PLAYER2;
        }else{
            curPlayerToken = TicTacToeLogic.PLAYER1;
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
