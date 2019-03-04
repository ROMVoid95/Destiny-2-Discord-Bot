package site.romvoid.forgebot.commands.admin;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;

public class commandChkRoles implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        
        
        
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String help() {
        return null;
    }

}
