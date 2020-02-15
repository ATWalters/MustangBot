package mustangBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class InviteCommand extends Command {

    private Member initiator;

    public InviteCommand(){
        this.name = "invite";
        this.help = "Create an invite link to the server to share with friends.";
        this.aliases = new String[] {"inv"};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] messageSent = event.getMessage().getContentRaw().split("");
        initiator = event.getMember();

        if(initiator.hasPermission(Permission.CREATE_INSTANT_INVITE)) {
            if (messageSent.length == 1) {
                event.reply("To invite someone give them this link: " + event.getTextChannel().createInvite().complete().getUrl());
            }
        }else{
            event.reply("You do not have permission to invite people, ask someone who does to do this");
        }
    }
}
