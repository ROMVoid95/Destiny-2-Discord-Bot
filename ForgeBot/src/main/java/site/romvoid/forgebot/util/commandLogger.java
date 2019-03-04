package site.romvoid.forgebot.util;

import java.awt.Color;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class commandLogger {


    public static void logCommand(String command, MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        String logchannel = MySQL.getValue(guild, "logchannel");

        if (logchannel.equals("0"))
            return;

        TextChannel channel = guild.getTextChannelById(logchannel);
        channel.sendTyping().queue();
        embedSender.sendPermanentEmbed("Command `" + prefix + command + "` was executed by **"
                + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + "**",
                channel, Color.cyan);
        System.out.println("Command " + prefix + command + " was executed by "
                + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());

    }
}
