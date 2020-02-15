package mustangBot.commands;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.commons.waiter.*;

import java.io.*;
import java.util.*;

public class ConnectFourCommand extends Command {

    private EventWaiter waiter;

    public ConnectFourCommand(EventWaiter w) throws IOException {
        this.name = "c4";
        this.help = "!!!NOT AVAILABLE YET!!!\nStarts a Connect Four game with another server member";
        this.aliases = new String[] {"cf"};
        this.waiter = w;
    }

    @Override
    protected void execute(CommandEvent event){

    }
}
