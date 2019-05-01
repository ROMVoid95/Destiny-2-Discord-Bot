package site.romvoid.forgebot.commands;

import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.Colors;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;
import site.romvoid.forgebot.util.destiny.Player;
import site.romvoid.forgebot.util.exemptions.NicknameExcemption;
import site.romvoid.forgebot.util.exemptions.PlayerNotFound;

public class commandMemberId implements Command {
	
    private static String removeBrackets(String name) {
        return name
                .replaceFirst(
                        Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                .replaceAll(" ", "");
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event)
            throws PlayerNotFound, NicknameExcemption {
        String nickname = null;
        if (args.length > 0) {
            String name = args[0];
            if (name.contains("["))
                nickname = removeBrackets(name);
            else
                nickname = args[0];

        } else {
            String name = event.getMember().getEffectiveName();
            if (name.contains("["))
                nickname = removeBrackets(name);
            else
                nickname = name;
        }

        String id = null;
        MessageChannel ch = event.getChannel();

        try {
            id = Player.getId(nickname);
            event.getChannel()
                    .sendMessage(new EmbedBuilder().setTitle("**Destiny 2 Membership ID**")
                            .setColor(Colors.EmeraldGreen)
                            .setDescription(nickname).addField("Member ID", id, false)
                            .build())
                    .queue();
        } catch (PlayerNotFound pnf) {
            String e = pnf.toString()
                    .replace("site.romvoid.forgebot.util.exemptions.PlayerNotFound: ", "");
            embedSender.sendPermanentEmbed(e, ch, Colors.Red);
        } catch (NicknameExcemption ne) {
            String e = ne.toString()
                    .replace("site.romvoid.forgebot.util.exemptions.NicknameExcemption: ", "");
            embedSender.sendPermanentEmbed(e, ch, Colors.Red);
        }
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
