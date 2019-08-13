package com.prime.commands.botowner;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.commands.moderation.CommandVerification;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;

import net.dv8tion.jda.core.entities.Message;

public class CommandAlarm extends CommandHandler {

    public CommandAlarm() {
        super(new String[]{"god"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.alarm", true, false), "Nice Stuff :scream:", "codingguy");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 0) {
            return EmbedUtil.message(EmbedUtil.error("Seriously?", ":facepalm:"));
        }
        switch (parsedCommandInvocation.getArgs()[0]) {
            case "codingguy":
                CommandVerification.toggleInspired();
                return EmbedUtil.message(EmbedUtil.success("Toggled", "Successfully toggled the `inspired`."));
        }
        return createHelpMessage();
    }
}
