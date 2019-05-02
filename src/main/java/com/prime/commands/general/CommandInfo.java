package com.prime.commands.general;

import com.prime.Prime;
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
import net.dv8tion.jda.core.entities.User;

public class CommandInfo extends CommandHandler {

    private String[] arrSupporter = {"Greatly Needed"};

    public CommandInfo() {
        super(new String[]{"info", "inf", "version"}, CommandCategory.GENERAL, new PermsNeeded("command.info", false, true), "Shows some information about the bot!", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        //Set some Vars
        Message message = parsedCommandInvocation.getMessage();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Colors.COLOR_PRIMARY);
        builder.setAuthor(Info.BOT_NAME + " - Information", message.getJDA().getSelfUser().getEffectiveAvatarUrl());
        StringBuilder authors = new StringBuilder();

        //Append on StringBuilder
        for (long authorId : Info.BOT_AUTHOR_IDS) {
            User authorUser = Prime.getJDA().getUserById(authorId);
            if (authorUser == null)
                authors.append(authorId).append("\n");
            else
                authors.append(authorUser.getName()).append("#").append(authorUser.getDiscriminator()).append("\n");
        }
        //Set the Embed Values
        builder.addField("Bot Name", Info.BOT_NAME, true);
        builder.addField("Bot Version", Info.BOT_VERSION, true);
        builder.addField("Bot Invite", "[Invite TheVoid](https://discordapp.com/api/oauth2/authorize?client_id=528635696001318928&permissions=2080898295&scope=bot)", true);
        builder.addField("Github Link", "[Github Link](" + Info.BOT_GITHUB + ")", true);
        builder.addField("Patreon Link", "[TheVoid Dev](https://www.patreon.com/romvoid)", true);
        builder.addField("Support Server", "[Link](https://discord.gg/hgMZy2R)", true);
        builder.addField("Donators", String.join("\n", arrSupporter), true);
        builder.addField("Devs", authors.toString(), false);
        return new MessageBuilder().setEmbed(builder.build()).build();
    }

}
