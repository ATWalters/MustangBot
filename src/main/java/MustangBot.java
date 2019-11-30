import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import mustangBot.events.*;
import mustangBot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import java.io.*;

public class MustangBot {
    public static void main(String[] args) throws Exception{

        //Reading discord bot token from token.txt and building new jda using that token
        File file = new File("D:\\School\\Programming\\Projects\\MustangBot\\token.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String token = br.readLine();
        JDA jda = new JDABuilder( token ).build();

        //Reading discord client id from client.txt and adding that to the command client
        File file2 = new File("D:\\School\\Programming\\Projects\\MustangBot\\client.txt");
        BufferedReader br2 = new BufferedReader(new FileReader(file2));
        String id = br2.readLine();

        //Adding an event waiter
        EventWaiter waiter = new EventWaiter();

        //Creating new JokeEvent object and calling fillJokes method
        JokeEvent jokeEventListener = new JokeEvent();
        jokeEventListener.fillJokes();

        //Making a command client
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId(id);
        builder.setPrefix("!");
        builder.setHelpWord("help");

        //Adding commands to the CommandClientBuilder
        builder.addCommand(new InviteCommand());
        builder.addCommand(new TicTacToeCommand(waiter));

        //Building the CommandClient
        CommandClient client = builder.build();

        //Adding each event to the listener
        jda.addEventListener(jokeEventListener);
        jda.addEventListener(new MarcoEvent());
        //jda.addEventListener(new TicTacToeEvent());
        jda.addEventListener(client);
        jda.addEventListener(waiter);
    }
}
