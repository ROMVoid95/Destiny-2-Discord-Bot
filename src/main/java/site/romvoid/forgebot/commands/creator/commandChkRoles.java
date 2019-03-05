package site.romvoid.forgebot.commands.creator;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;
import site.romvoid.forgebot.util.embedSender;

public class commandChkRoles implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        
        embedSender.notYet(event);
        
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String help() {
        return null;
    }

}
