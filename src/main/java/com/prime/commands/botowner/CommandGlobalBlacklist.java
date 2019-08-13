package com.prime.commands.botowner;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;
import com.prime.util.GlobalBlacklist;
import net.dv8tion.jda.core.entities.Message;

public class CommandGlobalBlacklist extends CommandHandler {

    public CommandGlobalBlacklist() {
        super(new String[]{"globalblacklist", "gbl"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.globalblacklist", true, false), "Ban a user from TheVoid.", "<@User>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getMessage().getMentionedUsers().size() == 1) {
            return createHelpMessage();
        }
        switch (parsedCommandInvocation.getArgs()[0]) {
            case "add":
                GlobalBlacklist.addToBlacklist(parsedCommandInvocation.getMessage().getMentionedUsers().get(0));
                return EmbedUtil.message(EmbedUtil.success("Success!", "Successfuly added " + parsedCommandInvocation.getMessage().getMentionedUsers().get(0).getName() + " to global blacklist."));
            case "remove":
                GlobalBlacklist.removeFromBlacklist(parsedCommandInvocation.getMessage().getMentionedUsers().get(0));
                return EmbedUtil.message(EmbedUtil.success("Success!", "Successfuly removed " + parsedCommandInvocation.getMessage().getMentionedUsers().get(0).getName() + " from the global blacklist."));
        }
        return createHelpMessage();
    }
}
