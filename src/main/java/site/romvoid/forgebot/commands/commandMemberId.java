package site.romvoid.forgebot.commands;

import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.destiny.UserMemberID;

public class commandMemberId implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

        String name = event.getMember().getEffectiveName();
        String nickname;

        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        String id = UserMemberID.getMemberId(nickname);
        event.getChannel().sendMessage(new EmbedBuilder().setAuthor(nickname)
                .addField("**Destiny 2 MembershipId**", id, false).build()).queue();
    }



    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("MemberId", event);
    }

    @Override
    public String help() {
        return null;
    }

}
