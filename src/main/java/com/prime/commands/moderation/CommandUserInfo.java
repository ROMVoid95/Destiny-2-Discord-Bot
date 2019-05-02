package com.prime.commands.moderation;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import java.time.OffsetDateTime;

public class CommandUserInfo extends CommandHandler {

    public CommandUserInfo() {
        super(new String[]{"userinfo", "whois"}, CommandCategory.MODERATION, new PermsNeeded("command.userinfo", false, false), "Returns some information about the specified user", "[@User]");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        Message message = parsedCommandInvocation.getMessage();
        User info;
        String[] args = parsedCommandInvocation.getArgs();
        if (args.length > 0) {
            if (message.getMentionedUsers().size() > 0)
                info = message.getMentionedUsers().get(0);
            else {
                return createHelpMessage();
            }
        } else {
            info = message.getAuthor();
        }

        Member user = message.getGuild().getMember(info);
        StringBuilder rawRoles = new StringBuilder();
        user.getRoles().forEach(r -> rawRoles.append(r.getName()).append(", "));
        StringBuilder roles = new StringBuilder(rawRoles.toString());
        if (!user.getRoles().isEmpty())
            roles.replace(rawRoles.lastIndexOf(","), roles.lastIndexOf(",") + 1, "");
        EmbedBuilder userinfo = new EmbedBuilder();
        userinfo.setColor(Colors.COLOR_PRIMARY);
        userinfo.setTitle("User information of " + user.getUser().getName());
        userinfo.setThumbnail(info.getAvatarUrl());
        userinfo.addField("Nickname", user.getEffectiveName(), false);
        userinfo.addField("User id", info.getId(), false);
        userinfo.addField("Status", user.getOnlineStatus().toString().replace("_", ""), false);
        if (user.getGame() != null)
            userinfo.addField("Game", user.getGame().getName(), false);
        userinfo.addField("Guild join date", formatDate(user.getJoinDate()), false);
        userinfo.addField("Roles", "`" + roles.toString() + "`", false);
        userinfo.addField("Discord join date", formatDate(info.getCreationTime()), false);
        userinfo.addField("Avatar url", (info.getAvatarUrl() != null) ? info.getAvatarUrl() : "https://rubicon.fun", true);
        return new MessageBuilder().setEmbed(userinfo.build()).build();
    }

    public String formatDate(OffsetDateTime date) {
        return date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear();
    }
}
