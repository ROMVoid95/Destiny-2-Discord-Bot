package com.prime.commands.settings;

import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandJoinMessage extends CommandHandler {
    public CommandJoinMessage() {
        super(new String[]{"joinmsg", "joinmessage"}, CommandCategory.SETTINGS,
                new PermsNeeded("command.joinmsg", false, false),
                "Set the server's join message!", "<Message(%user% for username, %guild% for guildname)>\ndisable/off");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 0)
            return createHelpMessage();
        String content = parsedCommandInvocation.getMessage().getContentDisplay().replace(parsedCommandInvocation.getPrefix() + parsedCommandInvocation.getCommandInvocation() + " ", "");
        if (content.equalsIgnoreCase("disable") || content.equalsIgnoreCase("false") || content.equalsIgnoreCase("0") || content.equalsIgnoreCase("off")) {
            Prime.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "joinmsg", "0");
            return new MessageBuilder().setEmbed(EmbedUtil.success("Disabled!", "Succesfully disabled joinmessages.").build()).build();
        }
        Prime.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "joinmsg", content);
        return new MessageBuilder().setEmbed(EmbedUtil.success("Enabled!", "Successfully set message to `" + content + "`.").build()).build();
    }
}