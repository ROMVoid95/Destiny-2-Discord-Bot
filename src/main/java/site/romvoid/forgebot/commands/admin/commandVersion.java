package site.romvoid.forgebot.commands.admin;

import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;
import site.romvoid.forgebot.util.STATIC;
import site.romvoid.forgebot.util.commandLogger;

public class commandVersion implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, final MessageReceivedEvent event) {
        event.getChannel().sendMessage("``` ForgeBot Version" + STATIC.VERSION + " by ROMVoid``` Github: https://github.com/ROMVoid95/ForgeBot/").queue();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.getMessage().delete().queue();
            }
        },2000);


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("version", event);

    }

    @Override
    public String help() {
        return null;
    }
}
