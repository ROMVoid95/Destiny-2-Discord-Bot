package site.romvoid.forgebot.commands.creator;

import java.awt.Color;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;
import site.romvoid.forgebot.core.permissionHandler;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;

public class commandPing implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {

        return false;

    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        final Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        message.delete().queue();
        
        if (permissionHandler.checkStaffRole(event)) {
            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }


        embedSender.sendEmbed("My Ping: `" + jda.getPing() + "`", channel, Color.CYAN);
        }



    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("ping", event);

    }

    @Override
    public String help() {
        return null;
    }
}
