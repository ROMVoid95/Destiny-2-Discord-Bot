package com.prime.commands.general;

import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import com.prime.util.Info;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandInvite extends CommandHandler {

    public CommandInvite() {
        super(new String[]{"invite", "inv"}, CommandCategory.GENERAL, new PermsNeeded("command.invite", false, true), "Gives you the invite-link of the bot.", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Colors.COLOR_SECONDARY);
        builder.setAuthor(Info.BOT_NAME + " - Invite", null, parsedCommandInvocation.getMessage().getJDA().getSelfUser().getAvatarUrl());
        builder.setDescription("[Invite TheVoid](https://discordapp.com/oauth2/authorize?client_id=528635696001318928&permissions=2080898295&scope=bot)\n" +
                "[Join Support Server](https://discord.gg/UrHvXY9)");
        return new MessageBuilder().setEmbed(builder.build()).build();
    }


}
