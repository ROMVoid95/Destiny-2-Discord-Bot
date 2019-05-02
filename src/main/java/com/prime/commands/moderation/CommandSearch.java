package com.prime.commands.moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;
import java.awt.*;

public class CommandSearch extends CommandHandler {

    public CommandSearch() {
        super(new String[]{"search", "find"}, CommandCategory.MODERATION, new PermsNeeded("command.search", false, false), "Searches for users, roles and channels with a specified name.", "<query>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        Message message = parsedCommandInvocation.getMessage();
        String[] args = parsedCommandInvocation.getArgs();
        TextChannel channel = message.getTextChannel();
        Guild guild = message.getGuild();

        if (args.length == 0) {
            return new MessageBuilder().setEmbed(EmbedUtil.info("Usage", "search <query>").build()).build();
        }
        StringBuilder query = new StringBuilder();
        for (String arg : args) {
            query.append(arg);
        }

        StringBuilder textchannels = new StringBuilder();
        StringBuilder voicechannels = new StringBuilder();
        StringBuilder members = new StringBuilder();
        StringBuilder roles = new StringBuilder();

        Message mymsg = channel.sendMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting textchannels ...").build()).complete();

        guild.getTextChannels().forEach(i -> {
            if (i.getName().toLowerCase().contains(query.toString().toLowerCase()))
                textchannels.append(i.getName() + "(`" + i.getId() + "`)").append("\n");
        });

        mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting voicechannels ...").build()).queue();


        guild.getVoiceChannels().forEach(i -> {
            if (i.getName().toLowerCase().contains(query.toString().toLowerCase()))
                voicechannels.append(i.getName() + "(`" + i.getId() + "`)").append("\n");
        });

        mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting users ...").build()).queue();


        guild.getMembers().forEach(i -> {
            if (i.getUser().getName().toLowerCase().contains(query.toString().toLowerCase()) || i.getEffectiveName().toLowerCase().contains(query.toString().toLowerCase()))
                members.append(i.getUser().getName() + "(`" + i.getUser().getId() + "`)").append("\n");
        });

        mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting roles ...").build()).queue();

        guild.getRoles().forEach(i -> {
            if (i.getName().toLowerCase().contains(query.toString().toLowerCase()))
                roles.append(i.getName() + "(`" + i.getId() + "`)").append("\n");
        });
        mymsg.delete().queue();
        try {
            EmbedBuilder results = new EmbedBuilder()
                    .setColor(Color.green)
                    .addField("**Textchannels**", textchannels.toString(), false)
                    .addField("**Voicechannles**", voicechannels.toString(), false)
                    .addField("**Members**", members.toString(), false)
                    .addField("**Roles**", roles.toString(), false);
            return new MessageBuilder().setEmbed(results.build()).build();
        } catch (IllegalArgumentException ex) {
            return new MessageBuilder().setEmbed(EmbedUtil.error("Error!", "Too many results!").build()).build();
        }

    }


}
