package com.prime.listener;


import com.prime.Prime;
import com.prime.util.SafeMessage;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        /* Leave message */
        String message = Prime.getMySQL().getGuildValue(event.getGuild(), "leavemsg").replace("%user%", event.getMember().getAsMention()).replace("%guild%", event.getGuild().getName());
        if (message == null) return;
        if (message.equalsIgnoreCase("0") || message.equalsIgnoreCase(" 0")) return;
        TextChannel channel = event.getGuild().getTextChannelById(Prime.getMySQL().getGuildValue(event.getGuild(), "channel"));
        if (channel == null) return;

        SafeMessage.sendMessage(channel, message);
    }

}
