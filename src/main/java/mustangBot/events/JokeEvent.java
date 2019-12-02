package mustangBot.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.*;
import java.util.ArrayList;

public class JokeEvent extends ListenerAdapter {
   //ArrayList to store jokes from jokes.txt
   private ArrayList<String> jokes = new ArrayList<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String messageSent = event.getMessage().getContentRaw();

        if(event.getAuthor().isBot()){
            return;
        }
        if(messageSent.equalsIgnoreCase( "joke")){
            int index = (int)(Math.random()*((this.jokes.size())));
            event.getChannel().sendMessage(this.jokes.get(index)).queue();
        }
    }

    //Method that fills the array with jokes from the file jokes.txt
    public void fillJokes() throws IOException {
        File file = new File("D:\\CS Side Projects\\MustangBot\\jokes.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int i = 0;
        while((st = br.readLine()) != null){
            this.jokes.add(i,st);
            i++;
        }
    }
}
