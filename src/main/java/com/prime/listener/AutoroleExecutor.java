package com.prime.listener;

import com.prime.Prime;
import com.prime.util.EmbedUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AutoroleExecutor extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Guild guild = e.getGuild();
        String roleEntry = Prime.getMySQL().getGuildValue(guild, "autorole");
        try {
            if (e.getMember().getUser().isBot()) return;
            if (roleEntry.equalsIgnoreCase("0")) return;
        } catch (Exception ignored) {

        }

        try {
            e.getGuild().getController().addRolesToMember(e.getMember(), e.getGuild().getRoleById(roleEntry)).queue();
        } catch (Exception ignored) {
            // Ignored because role can be deleted or higher than the bot role
            e.getGuild().getOwner().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(EmbedUtil.error("Error!", "Your autorole is invalid. That means the role doesn't exist, the id is invalid or the role is higher than bot's role. Please try again setting an autorole or choose an other role.").build()).queue());
        }

    }
}