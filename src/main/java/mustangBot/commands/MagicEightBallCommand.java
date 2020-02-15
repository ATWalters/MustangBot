package mustangBot.commands;

import com.jagrosh.jdautilities.command.*;
import net.dv8tion.jda.api.entities.*;

import java.io.*;
import java.util.*;

public class MagicEightBallCommand extends Command {

    private ArrayList<String> answers = new ArrayList<String>();
    private Member initiator;

    public MagicEightBallCommand() throws IOException {
        this.name = "8ball";
        this.aliases = new String[] {"magic8ball"};
        this.help = "A command to ask a Magic 8 Ball a question";
        this.arguments = "[Question to ask]";
        this.fillAnswers();
    }

    @Override
    protected void execute(CommandEvent event){
        String[] messageSent = event.getArgs().split(" ");
        initiator = event.getMember();

        if(messageSent.length < 2){
            event.reply(initiator.getAsMention() + ", I'm sorry but you did not ask a question.");
        }else{
            int index = (int)(Math.random()*((this.answers.size())));
            event.reply(this.answers.get(index));
        }
    }

    private void fillAnswers() throws IOException {
        File file = new File("D:\\CS Side Projects\\MustangBot\\8ball answers.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int i = 0;
        while ((st = br.readLine()) != null) {

            this.answers.add(i,st);
            i++;
        }
    }
}
