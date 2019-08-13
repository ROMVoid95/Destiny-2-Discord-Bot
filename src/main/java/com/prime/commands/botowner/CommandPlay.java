package com.prime.commands.botowner;

import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Configuration;
import com.prime.util.EmbedUtil;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

public class CommandPlay extends CommandHandler {

    public CommandPlay() {
        super(new String[]{"botplay", "status", "changestat"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.botplay", true, false), "Change bot's playing status.", "<text>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        Configuration configuration = Prime.getConfiguration();
        String configKey = "playingStatus";
        if (!configuration.has(configKey)) {
            configuration.set(configKey, "0");
        }
        if (parsedCommandInvocation.getArgs().length == 0) {
            configuration.set(configKey, "0");
            return null;
        }
        StringBuilder message = new StringBuilder();
        for (String s : parsedCommandInvocation.getArgs())
            message.append(s).append(" ");

        Prime.getConfiguration().set(configKey, message.toString());
        parsedCommandInvocation.getMessage().getJDA().getPresence().setGame(Game.playing(message.toString()));

        return new MessageBuilder().setEmbed(EmbedUtil.success("Status set!", "Successfully set the playing status!").build()).build();
    }
}
