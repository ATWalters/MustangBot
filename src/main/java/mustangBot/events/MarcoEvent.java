package mustangBot.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MarcoEvent extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String messageSent = event.getMessage().getContentRaw();

        if(event.getAuthor().isBot()){
            return;
        }
        if(messageSent.equalsIgnoreCase("MarcoEvent")){
            event.getChannel().sendMessage("Polo!").queue();
        }
    }
}
