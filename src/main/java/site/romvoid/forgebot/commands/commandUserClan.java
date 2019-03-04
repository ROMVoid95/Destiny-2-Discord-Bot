package site.romvoid.forgebot.commands;

import java.awt.Color;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.destiny.clan.ClanTag;
import site.romvoid.forgebot.util.exemptions.IndexExemption;

public class commandUserClan implements Command {

    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws IndexExemption, Exception {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();
        String clanName = ClanTag.getName(nickname);
        String memCount = ClanTag.memberCount(nickname);
        String clanMotto = ClanTag.clanMoto(nickname);
        String userRank = ClanTag.getUserRank(nickname);
        String sign = ClanTag.callSign(nickname);
        String join = ClanTag.join(nickname);
        if (clanName != null) {
            event.getTextChannel()
                    .sendMessage(new EmbedBuilder().setColor(Color.green)
                            .setThumbnail("https://i.imgur.com/gAXSriS.jpg")
                            .setTitle(nickname + " Clan Information \n ")
                            .addField("**" + clanName + "** [" + sign + "] \n",
                                    "*" + clanMotto + "* \n\n " + "**Members**: " + memCount
                                            + "\n **Membership**: " + join,
                                    true)
                            .addField("**Rank**", userRank + " \n", true)
                            .build())
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
