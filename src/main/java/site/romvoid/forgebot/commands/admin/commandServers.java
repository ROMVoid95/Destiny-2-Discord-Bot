package site.romvoid.forgebot.commands.admin;

import java.awt.Color;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;


public class commandServers implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        PrivateChannel prich = author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        message.delete().queue();

        String out = "\n This bot is running on the following servers: \n";

        for (Guild g : event.getJDA().getGuilds()){
            out += g.getName() + " (" + g.getId() + ")  \n";
        }

        embedSender.sendPermanentEmbed(out, prich, Color.CYAN);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("servers", event);

    }

    @Override
    public String help() {
        return null;
    }
}
