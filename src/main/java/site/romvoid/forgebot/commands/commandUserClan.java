package site.romvoid.forgebot.commands;

import java.awt.Color;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.destiny.Player;
import site.romvoid.forgebot.util.destiny.clan.ClanTag;

public class commandUserClan implements Command {

    private static String removeBrackets(String name) {
        return name
                .replaceFirst(
                        Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                .replaceAll(" ", "");
    }

    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (args.length > 0) {
            name = args[0];
            if (name.contains("["))
                nickname = removeBrackets(name);
            else
                nickname = args[0];
        }

        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        String memberId = Player.getId(nickname);
        String clanName = ClanTag.getName(memberId);
        String memCount = ClanTag.memberCount(memberId);
        String clanMotto = ClanTag.clanMoto(memberId);
        String userRank = ClanTag.getUserRank(memberId);
        String sign = ClanTag.callSign(memberId);
        String join = ClanTag.join(memberId);
        if (clanName != null) {
            event.getTextChannel()
                    .sendMessage(new EmbedBuilder().setColor(Color.green)
                            .setThumbnail("https://i.imgur.com/gAXSriS.jpg")
                            .setTitle(nickname + " Clan Information \n ")
                            .addField("**" + clanName + "** [" + sign + "] \n",
                                    "*" + clanMotto + "* \n\n " + "**Members**: " + memCount
                                            + "\n **Membership**: " + join,
                                    true)
                            .addField("**Rank**", userRank + " \n", true).build())
                    .queue();
        } else {
            event.getTextChannel()
                    .sendMessage(new EmbedBuilder().setColor(Color.red)
                            .setThumbnail("https://i.imgur.com/gAXSriS.jpg")
                            .setTitle(nickname + " Doesn't appear to be in a clan \n ")
                            .setDescription(
                                    "If this is incorrect please check your discord nickname")
                            .build())
                    .queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("clan", event);
    }

    @Override
    public String help() {
        return null;
    }



}
