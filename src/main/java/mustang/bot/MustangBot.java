package mustang.bot;

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

        //Creating new Joke object and calling fillJokes method
        Joke jokeListener = new Joke();
        jokeListener.fillJokes();

        //Adding each event to the listener
        jda.addEventListener(jokeListener);
        jda.addEventListener(new Marco());
        jda.addEventListener(new TTT());
    }
}
