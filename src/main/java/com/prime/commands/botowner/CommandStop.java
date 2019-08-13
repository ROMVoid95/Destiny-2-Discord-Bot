package com.prime.commands.botowner;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;


public class CommandStop extends CommandHandler {

    public CommandStop() {
        super(new String[]{"botstop"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.botstop", true, false), "Stops the bot.", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        parsedCommandInvocation.getMessage().getTextChannel().sendMessage(new EmbedBuilder().setDescription(":battery: System Shutdown :battery:").build()).queue();
        System.exit(0);
        return null;
    }
}
