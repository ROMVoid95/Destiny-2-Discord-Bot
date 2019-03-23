package site.romvoid.forgebot.commands.admin;

import java.util.List;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
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
        Message message = event.getMessage();
                message.delete().queue();
                String w = "274681770421518336";
                String h = event.getJDA().getUserById(w).getAsMention().toString();
                event.getChannel().sendMessage("Congrats to " + h + "for submitting the winning name!").queue();
        embedSender.hello(event);
        
        
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String help() {
        return null;
    }

}
