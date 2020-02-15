package mustangBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class InviteCommand extends Command {

    public InviteCommand(){
        this.name = "invite";
        this.help = "Create an invite link to the server to share with friends.";
        this.aliases = new String[] {"inv"};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] messageSent = event.getMessage().getContentRaw().split(" ");

        if (messageSent.length == 1) {
            event.reply("To invite someone give them this link: " + event.getTextChannel().createInvite().complete().getUrl());
        }
    }
}
