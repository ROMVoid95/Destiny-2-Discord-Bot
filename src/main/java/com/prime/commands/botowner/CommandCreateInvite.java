package com.prime.commands.botowner;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;
import com.prime.util.Logger;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandCreateInvite extends CommandHandler {

    public CommandCreateInvite() {
        super(new String[]{"createinvite", "minv", "createinv", "newinv"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.createinvite", true, false), "Creates an invite", "<guildid>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        try {
            return new MessageBuilder().append(parsedCommandInvocation.getMessage().getJDA().getGuildById(parsedCommandInvocation.getArgs()[0]).getTextChannels().get(0).createInvite().complete().getURL()).build();
        } catch (Exception ex) {
            Logger.info("Create Invite: " + ex.getMessage());
            return new MessageBuilder().setEmbed(EmbedUtil.error("Error!", "An error occurred!").build()).build();
        }
    }
}