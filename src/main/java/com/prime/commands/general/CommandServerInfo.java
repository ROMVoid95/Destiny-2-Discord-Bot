package com.prime.commands.general;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import java.time.OffsetDateTime;


public class CommandServerInfo extends CommandHandler {

    public CommandServerInfo() {
        super(new String[]{"serverinfo", "guildinfo"}, CommandCategory.GENERAL, new PermsNeeded("command.serverinfo", false, true), "Returns some information about the current/an other server", "[serverid]");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        boolean guildexist = Guildexist(parsedCommandInvocation);
        if (guildexist) {
            Guild guild = Prime.getJDA().getGuildById(parsedCommandInvocation.getArgs()[0]);
            StringBuilder rawRoles = new StringBuilder();
            guild.getRoles().forEach(r -> rawRoles.append(r.getName()).append(", "));
            StringBuilder roles = new StringBuilder(rawRoles.toString());
            roles.replace(rawRoles.lastIndexOf(","), roles.lastIndexOf(",") + 1, "");
            EmbedBuilder serverInfo = new EmbedBuilder();
            serverInfo.setColor(Colors.COLOR_PRIMARY);
            serverInfo.setTitle(":desktop: Serverinfo of " + guild.getName());
            serverInfo.setThumbnail(guild.getIconUrl());
            serverInfo.addField("ID", "`" + guild.getId() + "`", false);
            serverInfo.addField("Guildname", "`" + guild.getName() + "`", false);
            serverInfo.addField("Server region", guild.getRegion().toString(), false);
            serverInfo.addField("Members", String.valueOf(guild.getMembers().size()), false);
            serverInfo.addField("Textchannels", String.valueOf(guild.getTextChannels().size()), false);
            serverInfo.addField("Voicechannels", String.valueOf(guild.getVoiceChannels().size()), false);
            serverInfo.addField("Roles", String.valueOf(guild.getRoles().size()) + "\n ```" + roles.toString() + "```", false);
            serverInfo.addField("Server owner", guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), false);
            if (hasicon(guild)) {
                serverInfo.addField("Server icon url", guild.getIconUrl(), false);
            }
            serverInfo.addField("Server Creation Date", formatDate(guild.getCreationTime()), false);
            return new MessageBuilder().setEmbed(serverInfo.build()).build();
        } else {
            Guild guild = parsedCommandInvocation.getGuild();
            StringBuilder rawRoles = new StringBuilder();
            guild.getRoles().forEach(r -> rawRoles.append(r.getName()).append(", "));
            StringBuilder roles = new StringBuilder(rawRoles.toString());
            roles.replace(rawRoles.lastIndexOf(","), roles.lastIndexOf(",") + 1, "");
            EmbedBuilder serverInfo = new EmbedBuilder();
            serverInfo.setColor(Colors.COLOR_PRIMARY);
            serverInfo.setTitle(":desktop: Serverinfo of " + guild.getName());
            serverInfo.setThumbnail(guild.getIconUrl());
            serverInfo.addField("ID", "`" + guild.getId() + "`", false);
            serverInfo.addField("Guildname", "`" + guild.getName() + "`", false);
            serverInfo.addField("Server region", guild.getRegion().toString(), false);
            serverInfo.addField("Members", String.valueOf(guild.getMembers().size()), false);
            serverInfo.addField("Textchannels", String.valueOf(guild.getTextChannels().size()), false);
            serverInfo.addField("Voicechannels", String.valueOf(guild.getVoiceChannels().size()), false);
            serverInfo.addField("Roles", String.valueOf(guild.getRoles().size()) + "\n ```" + roles.toString() + "```", false);
            serverInfo.addField("Server owner", guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), false);
            if (hasicon(guild)) {
                serverInfo.addField("Server icon url", guild.getIconUrl(), false);
            }
            serverInfo.addField("Server Creation Date", formatDate(guild.getCreationTime()), false);
            return new MessageBuilder().setEmbed(serverInfo.build()).build();
        }


    }

    public String formatDate(OffsetDateTime date) {
        return date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear();
    }

    public boolean Guildexist(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        try {
            Guild guild = Prime.getJDA().getGuildById(parsedCommandInvocation.getArgs()[0]);
            guild.getName();
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {
            return false;

        }
        return true;
    }

    public boolean hasicon(Guild g) {
        try {
            String url = g.getIconUrl();
            url.toLowerCase();
        } catch (NullPointerException ignored) {
            return false;

        }
        return true;
    }

}
