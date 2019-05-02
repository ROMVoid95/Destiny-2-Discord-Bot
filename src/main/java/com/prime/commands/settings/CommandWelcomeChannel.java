package com.prime.commands.settings;

import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandWelcomeChannel extends CommandHandler {

    public CommandWelcomeChannel() {
        super(new String[]{"welcomechannel", "welchannel", "joinchannel"}, CommandCategory.SETTINGS,
                new PermsNeeded("command.channel", false, false),
                "Set the Server Welcome Channel!", "<#channel>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        //Check if Channel got Mentioned
        if (parsedCommandInvocation.getMessage().getMentionedChannels().size() <= 0)
            return new MessageBuilder().setEmbed(new EmbedBuilder().setDescription(getParameterUsage()).build()).build();
        //Get the Mentioned Channel
        String ch = parsedCommandInvocation.getMessage().getMentionedChannels().get(0).getId();
        //Update MySql
        Prime.getMySQL().updateGuildValue(parsedCommandInvocation.getMessage().getGuild(), "channel", ch);
        return new MessageBuilder().setEmbed(new EmbedBuilder().setDescription(":white_check_mark: Successfully set the Event-Channel!").build()).build();
    }
}
