package mustangBot.commands;

import com.jagrosh.jdautilities.command.*;

public class MagicEightBallCommand extends Command {

    public MagicEightBallCommand(){
        this.name = "8ball";
        this.aliases = new String[] {"magic8ball"};
        this.help = "A command to ask a Magic 8 Ball a question";
        this.arguments = "[Question to ask]";
    }

    @Override
    protected void execute(CommandEvent event){

    }
}
