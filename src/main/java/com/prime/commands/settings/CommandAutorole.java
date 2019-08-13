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

public class CommandAutorole extends CommandHandler {
    public CommandAutorole() {
        super(new String[]{"autorole", "arole", "ar"}, CommandCategory.ADMIN, new PermsNeeded("command.autorole", false, false), "Set the Autorole. Triggers when a User Joins your Guild", "<@Role/Rolename>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length < 1) {
            return createHelpMessage();
        }
        if (parsedCommandInvocation.getMessage().getMentionedRoles().size() < 1) {
            String toset = parsedCommandInvocation.getMessage().getGuild().getRolesByName(parsedCommandInvocation.getArgs()[0], true).get(0).getId();
            Prime.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "autorole", toset);
        } else {
            Prime.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "autorole", parsedCommandInvocation.getMessage().getMentionedRoles().get(0).getId());
        }
        return new MessageBuilder().setEmbed(EmbedUtil.success("Succes", "Succesfully set the Autorole!").build()).build();
    }
}
