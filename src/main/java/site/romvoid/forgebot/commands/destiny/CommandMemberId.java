package site.romvoid.forgebot.commands.destiny;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.AbstractCommand;
import site.romvoid.forgebot.util.Parse;
import site.romvoid.forgebot.util.destiny.UserMemberID;

public class CommandMemberId extends AbstractCommand {

    @Override
    public String getDescription() {
        return "Get your Destiny 2 membership ID";
    }

    @Override
    public String getCommand() {
        return "memberid";
    }

    @Override
    public String[] getUsage() {
        return new String[] {
                "memberid"
        };
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "memid"
        };
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("[")) {
            nickname = Parse.nickname(name);
        } else {
            nickname = event.getMember().getEffectiveName();
        }
        try {
           String id = UserMemberID.getMemberId(nickname);
           event.getChannel().sendMessage(new EmbedBuilder().setAuthor(nickname)
                   .addField("**Destiny 2 MembershipId**", id, false).build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
