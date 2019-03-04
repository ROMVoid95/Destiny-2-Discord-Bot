package site.romvoid.forgebot.commands;


import java.awt.Color;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.destiny.RaidReport;
import site.romvoid.forgebot.util.destiny.UserMemberID;

public class commandRaidReport implements Command {

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

        if (id == null) {
            event.getTextChannel()
                    .sendMessage(new EmbedBuilder().setColor(Color.red)
                            .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n")
                            .build())
                    .queue();
        }
        if (id != null) {
            String clears = RaidReport.getClearsRank(id);
            String speed = RaidReport.getSpeedRank(id);

            if (RaidReport.getClearsTier() != "Challenger") {

            }
            if (clears.contains("Challenger")) {

                String[] splitClears = clears.split(",", 2);
                String clearsRank = splitClears[0];
                String clearsBody = splitClears[1];
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setTitle("**Raid.Report Ranks For**")
                        .setDescription("*" + nickname + "*  ")
                        .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                        .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                        .addField("Full Clears Rank #" + clearsRank, clearsBody, true)
                        .addField("Speed Rank", speed, true).addBlankField(false)
                        .setFooter("Provided by © 2019 DestinyRaidReport",
                                "https://i.imgur.com/JWCUxyx.png")
                        .build()).queue();

            } else if (speed.contains("Challenger")) {

                String[] splitSpeed = speed.split(",", 2);
                String speedRank = splitSpeed[0];
                String speedBody = splitSpeed[1];
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setTitle("**Raid.Report Ranks For**")
                        .setDescription("*" + nickname + "*  ")
                        .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                        .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                        .addField("Full Clears Rank", clears, true)
                        .addField("Speed Rank #" + speedRank, speedBody, true).addBlankField(false)
                        .setFooter("Provided by © 2019 DestinyRaidReport",
                                "https://i.imgur.com/JWCUxyx.png")
                        .build()).queue();

            } else {
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setTitle("**Raid.Report Ranks For**")
                        .setDescription("*" + nickname + "*  ")
                        .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                        .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                        .addField("Full Clears Rank", clears, true)
                        .addField("Speed Rank", speed, true).addBlankField(false)
                        .setFooter("Provided by © 2019 DestinyRaidReport",
                                "https://i.imgur.com/JWCUxyx.png")

                        .build()).queue();
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("report", event);
    }

    @Override
    public String help() {
        return null;
    }

}
