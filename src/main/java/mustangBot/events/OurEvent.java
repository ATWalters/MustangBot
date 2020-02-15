package mustangBot.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OurEvent extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String messageSent = event.getMessage().getContentRaw().toLowerCase();

        if(event.getAuthor().isBot()){
            return;
        }

        if(messageSent.contains("our ")){
            event.getChannel().sendMessage("*communism intensifies*").queue();
        }
    }
}
