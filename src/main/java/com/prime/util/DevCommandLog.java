package com.prime.util;

import com.prime.Prime;
import com.prime.command.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 * @author ROMVoid
 */
public class DevCommandLog {

    private static final String channelId = Info.CMD_LOG_CHANNEL;

    public static void log(CommandManager.ParsedCommandInvocation invocation) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(
                "User: " + invocation.getAuthor().getName() + "(" + invocation.getAuthor().getId() + ")\n" +
                        "Guild: " + invocation.getGuild().getName() + "(" + invocation.getGuild().getId() + ")\n" +
                        "-------------------------\n" +
                        "Command: " + invocation.getCommandInvocation() + "\n" +
                        "Bot's ping: " + Prime.getJDA().getPing() + "ms");
        try {
            Prime.getJDA().getTextChannelById(channelId).sendMessage(builder.build()).queue();
        } catch (Exception ignore) {

        }
    }
}
