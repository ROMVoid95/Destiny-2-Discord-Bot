package site.romvoid.forgebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.exemptions.NicknameExcemption;

public interface Command {

    boolean called(String[] args, MessageReceivedEvent event);

    void action(String[] args, MessageReceivedEvent event) throws Exception, NicknameExcemption;

    void executed(boolean success, MessageReceivedEvent event);

    String help();
}
