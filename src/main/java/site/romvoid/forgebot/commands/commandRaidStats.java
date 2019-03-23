package site.romvoid.forgebot.commands;


import java.awt.Color;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;
import site.romvoid.forgebot.util.destiny.Player;
import site.romvoid.forgebot.util.destiny.raids.RaidStats;
import site.romvoid.forgebot.util.destiny.raids.ReqRaids;

public class commandRaidStats implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        String id = Player.getId(nickname);
        if (id != null) {
            RaidStats rs = ReqRaids.collectAllRaidStats(nickname);
            embedSender.raidStats(event, rs);
        } else {
            event.getTextChannel()
                    .sendMessage(new EmbedBuilder().setColor(Color.red)
                            .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n")
                            .build())
                    .queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("rstats", event);
    }

    @Override
    public String help() {
        return null;
    }

}
