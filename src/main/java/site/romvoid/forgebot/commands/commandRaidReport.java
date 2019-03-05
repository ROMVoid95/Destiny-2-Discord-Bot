package site.romvoid.forgebot.commands;


import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;
import site.romvoid.forgebot.util.destiny.RaidReport;
import site.romvoid.forgebot.util.destiny.UserMemberID;

public class commandRaidReport implements Command {
    
    private static String ch = "Challenger";

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
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

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

        String id = UserMemberID.getMemberId(nickname);

        if (id == null) {
            embedSender.noId(event);
        }
        if (id != null) {
            String clears = RaidReport.getClearsRank(id);
            String speed = RaidReport.getSpeedRank(id);

            if (clears.contains(ch) && !speed.contains(ch)) {
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


            } else if (speed.contains(ch) && !clears.contains(ch)) {
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


            } else if (speed.contains(ch) && (clears.contains(ch))) {
                String[] splitSpeed = speed.split(",", 2);
                String speedRank = splitSpeed[0];
                String speedBody = splitSpeed[1];
                String[] splitClears = clears.split(",", 2);
                String clearsRank = splitClears[0];
                String clearsBody = splitClears[1];
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setTitle("**Raid.Report Ranks For**")
                        .setDescription("*" + nickname + "*  ")
                        .appendDescription("  [**Link**](https://raid.report/pc/" + id + ")")
                        .addBlankField(false).setThumbnail("https://i.imgur.com/JWCUxyx.png")
                        .addField("Full Clears Rank #" + clearsRank, clearsBody, true)
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
