package site.romvoid.forgebot.util;


import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.destiny.raids.RaidStats;
import site.romvoid.forgebot.util.destiny.raids.Raids;

public class embedSender {
    
    public static void sendEmbed(String content, MessageChannel channel, Color color){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color);
        Message mymsg = channel.sendMessage(embed.build()).complete();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mymsg.delete().queue();
            }
        }, 5000);
    }
    
    public static void sendEmbedWithImg(String content, MessageChannel channel, Color color, String url){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color).setThumbnail(url);
        Message mymsg = channel.sendMessage(embed.build()).complete();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mymsg.delete().queue();
            }
        }, 5000);
    }
    
    public static void sendPermanentEmbed(String content, MessageChannel channel, Color color){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color);
        channel.sendMessage(embed.build()).complete();

    }
    
    public static void raidStats(MessageReceivedEvent event, RaidStats rs) {

        MessageChannel channel = event.getChannel();
        String nickname = event.getMember().getEffectiveName();

        Raids lev = rs.getLev();
        Raids eow = rs.getEow();
        Raids sos = rs.getSos();
        Raids lw = rs.getLw();
        Raids sp = rs.getSp();

        EmbedBuilder embed = new EmbedBuilder()

                .setAuthor(nickname, null, event.getAuthor().getEffectiveAvatarUrl())
                .setDescription(
                        String.format("Has completed **%s** total raids", rs.getTotalCompletion()))
                .setColor(Color.green).setThumbnail("https://i.imgur.com/rS2b88z.png")
                .addField(String.format("__Leviathan__", lev.getCompletions()),
                        String.format("**N:** %d\t|\t** P:** %d\t|\t** G:** %d\n\t",
                                lev.getNormalCompletions(), lev.getPrestigeCompletions(),
                                lev.getGuidedCompletions()),
                        true)
                .addField(String.format("__Eater of Worlds__", eow.getCompletions()),
                        String.format("**N:** %d\t|\t** P:** %d\t|\t** G:** %d\n\t",
                                eow.getNormalCompletions(), eow.getPrestigeCompletions(),
                                eow.getGuidedCompletions()),
                        true)
                .addField(String.format("__Spire of Stars__", sos.getCompletions()),
                        String.format("**N:** %d\t|\t** P:** %d\t|\t** G:** %d\n\t",
                                sos.getNormalCompletions(), sos.getPrestigeCompletions(),
                                sos.getGuidedCompletions()),
                        true)
                .addField(String.format("__Last Wish__", lw.getCompletions()),
                        String.format("**N:** %d\t|\t** G:** %d", lw.getNormalCompletions(),
                                lw.getGuidedCompletions()),
                        true)
                .addField(String.format("__Scourge of the Past__", sp.getCompletions()),
                        String.format("**N:** %d\t|\t** G:** %d", sp.getNormalCompletions(),
                                sp.getGuidedCompletions()),
                        true);
        channel.sendMessage(embed.build()).queue();
    }
    
    public static void error(MessageReceivedEvent event) {
        
        MessageChannel channel = event.getChannel();
        String nickname = event.getMember().getEffectiveName();
        
        EmbedBuilder err = new EmbedBuilder()
                .setAuthor(nickname, null, event.getAuthor().getEffectiveAvatarUrl())
                .addField("Nickname does not match correct format", "Please edit your nickname\n\n", false)
                .addField("----Correct Format----" , "`[region] battlenetID#1234`\nExample:\n[NA] ROMVoid#1909", true)
                .addField("Acceptable Regions", "**NA:** North America\n**EU:** Europe\n** AS:** Asia", true)
                .setColor(Color.red)
                .setTitle(" :no_entry:  ERROR  :no_entry: ")
                .addField("Please visit channel below for more info", "<#519325355451088928>", false);
               channel.sendMessage(err.build()).queue();
        
    }
    
    public static void oneChallenger(MessageReceivedEvent event) {
        String nickname = event.getMember().getEffectiveName();
        MessageChannel channel = event.getChannel();
        String id = null;
        String rank = null;
        String clears = null;
        String speed = null;
        
        EmbedBuilder beta = new EmbedBuilder()
                .setTitle("**Raid.Report Ranks For**")
                .setDescription("*" + nickname + "*  ")
                .appendDescription("  [**Link**](https://raid.report/pc/"+id+")")
                .addBlankField(false)
                .setThumbnail("https://i.imgur.com/JWCUxyx.png")
                .addField("Full Clears Rank #" + rank, clears, true)
                .addField("Speed Rank", speed, true)
                .addBlankField(false)
                .setFooter("Provided by © 2019 DestinyRaidReport", "https://i.imgur.com/JWCUxyx.png");
               channel.sendMessage(beta.build()).queue();
    }
    
    public static void notYet(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder beta = new EmbedBuilder()
                .setTitle("**Raid.Report Ranks For**")
                .setColor(Color.ORANGE)
                .addField("This command has not been fully implemented", "“I’m just a simple man trying to make my way in the universe.” — Jango Fett", false);
        channel.sendMessage(beta.build()).queue();
    }
    
    public static void noId(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder beta = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n");
        channel.sendMessage(beta.build()).queue();
    }
    
    public static void hasClearsRank(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder beta = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n");
        channel.sendMessage(beta.build()).queue();
    }
    
    public static void hasSpeedRank(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder beta = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n");
        channel.sendMessage(beta.build()).queue();
    }
    
    public static void hasBothRank(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder beta = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n");
        channel.sendMessage(beta.build()).queue();
    }
    
    public static void hasNoRank(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder beta = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription("**User Doesn't have an active Destiny 2 Account**\n\n");
        channel.sendMessage(beta.build()).queue();
    }

}
