package com.prime.commands.settings;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import java.util.concurrent.TimeUnit;

public class CommandPrefix extends CommandHandler {
    public CommandPrefix() {
        super(new String[]{"prefix", "pr"}, CommandCategory.SETTINGS,
                new PermsNeeded("command.prefix", false, false),
                "Set the Server Prefix!", "<prefix>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation p, UserPermissions userPermissions) {
        if (p.getArgs().length <= 1) {
            MessageChannel ch = p.getMessage().getTextChannel();

            if (p.getArgs().length == 0) {
                Prime.getMySQL().updateGuildValue(p.getMessage().getGuild(), "prefix", ">>");
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor("Prefix updated", null, p.getMessage().getGuild().getIconUrl());
                builder.setDescription(":white_check_mark: Successfully changed prefix to `>>`");
                ch.sendMessage(builder.build()).queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
            } else {
                Prime.getMySQL().updateGuildValue(p.getMessage().getGuild(), "prefix", p.getArgs()[0]);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor("Prefix updated", null, p.getMessage().getGuild().getIconUrl());
                builder.setDescription(":white_check_mark: Successfully changed prefix to `" + p.getArgs()[0] + "`");
                ch.sendMessage(builder.build()).queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
            }
        } else {
            return createHelpMessage();
        }

        return null;
    }
}
