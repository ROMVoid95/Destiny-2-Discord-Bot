package com.prime.commands.botowner;

import static com.prime.util.EmbedUtil.*;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import net.dv8tion.jda.core.entities.Message;

public class CommandDBGuild extends CommandHandler {
    public CommandDBGuild() {
        super(new String[]{"dbguild", "dbguilds"}, CommandCategory.BOT_OWNER,
                new PermsNeeded("command.dbguild", true, false),
                "Manage database guild entries.", "<add | remove | default> <ServerID>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 2) {
            String option = parsedCommandInvocation.getArgs()[0];
            String serverID = parsedCommandInvocation.getArgs()[1];

            switch (option) {
                case "default":
                    Prime.getMySQL().deleteGuild(serverID);
                    Prime.getMySQL().createGuildServer(serverID);
                    return message(success("Server set to default", "The Server with the ID " + serverID + " has been set to default."));
                case "add":
                    Prime.getMySQL().createGuildServer(serverID);
                    return message(success("Server added", "The Server with the ID " + serverID + " has been added successfully."));
                case "remove":
                    Prime.getMySQL().deleteGuild(serverID);
                    return message(success("Server removed", "The Server with the ID " + serverID + " has been removed successfully."));
                default:
                    return message(error("Invalid parameter", option + " is not an valid parameter."));
            }
        } else {
            return message(error("Not enough arguments", "You forgot to add the option or server's ID."));
        }
    }

}
