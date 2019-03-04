package site.romvoid.forgebot.commands;

import java.awt.Color;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;

public class commandBug implements Command {
    private String bug = "";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        message.delete().queue();

        User romVoid = jda.getUserById("393847930039173131");
        PrivateChannel romvoid = romVoid.openPrivateChannel().complete();
        
        for(int i = 0; i< args.length; i++){
            bug += args[i] + " ";
        }
        romvoid.sendMessage("The user" + author.getAsMention() + " has reported ```" + bug + "```").queue();
        embedSender.sendEmbed("Your bug was succesfully submited", event.getTextChannel(), Color.green);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("bug", event);

    }

    @Override
    public String help() {
        return null;
    }



}
