package mustangBot.events;

import mustangBot.logic.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TTT extends ListenerAdapter {

    private TicTacToe board;
    private char curPlayer;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        String user1 = event.getMember().getAsMention();

        if(event.getAuthor().isBot()){
            return;
        }

        if(messageSent[0].equalsIgnoreCase("ttt")){
            board = new TicTacToe(3, 3, 3);
            curPlayer = TicTacToe.PLAYER1;
            event.getChannel().sendMessage("To place a token on the board type   \"tplace <row> <column>\"   " +
                    "replacing the row and column with the location to place your marker " + user1).queue();
            event.getChannel().sendMessage(board.toString()).queue();
            event.getChannel().sendMessage("Player " + curPlayer + " it is your turn!").queue();
        }

        if(messageSent[0].equalsIgnoreCase("tplace")){
            int row = Integer.parseInt(messageSent[1]);
            int col = Integer.parseInt(messageSent[2]);
            if(!board.checkSpace(row, col)){
                event.getChannel().sendMessage("That space is unavailable, pick another location by re-typing  \"tplace <row> <col>").queue();
            }else{
                board.placeMarker(row, col, curPlayer);
                event.getChannel().sendMessage(board.toString()).queue();
                if (board.checkForDraw()) {
                    event.getChannel().sendMessage("Game is a draw! If you want to play again type  \"ttt").queue();
                } else if (board.checkForWinner(row, col, curPlayer)) {
                    event.getChannel().sendMessage("Player " + curPlayer + " wins! If you want to play again type  \"ttt").queue();
                }else {
                    this.swapPlayer();
                    event.getChannel().sendMessage("Player " + curPlayer + " it is your turn!").queue();
                }
            }
        }
    }

    private void swapPlayer(){
        if(curPlayer == TicTacToe.PLAYER1){
            curPlayer = TicTacToe.PLAYER2;
        }else{
            curPlayer = TicTacToe.PLAYER1;
        }
    }
}
