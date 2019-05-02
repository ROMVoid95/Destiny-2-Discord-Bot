package com.prime.listener;

import com.prime.Prime;
import com.prime.commands.moderation.CommandVerification;
import com.prime.features.VerificationKickHandler;
import com.prime.features.VerificationUserHandler;
import com.prime.util.SafeMessage;
import com.prime.util.StringUtil;
import java.util.Calendar;
import java.util.Date;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class VerificationListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!CommandVerification.setups.containsKey(event.getGuild())) return;
        if (!CommandVerification.setups.get(event.getGuild()).author.equals(event.getAuthor())) return;
        CommandVerification.VerificationSetup setup = CommandVerification.setups.get(event.getGuild());
        Message message = setup.message;
        Message response = event.getMessage();
        response.delete().queue();
        if (setup.step == 1)
            CommandVerification.setupStepOne(message, response);
        else if (setup.step == 2)
            CommandVerification.setupStepTwo(message, response);
        else if (setup.step == 3)
            CommandVerification.setupStepThree(message, response);
        else if (setup.step == 5)
            CommandVerification.setupStepFive(message, response);
        else if (setup.step == 6)
            CommandVerification.setupStepSix(message, response);
        else if (setup.step == 7)
            CommandVerification.setupStepSeven(message, response);
    }

    /**
     * Deactivate verification when channel got deleted
     */
    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        if (!Prime.getMySQL().verificationEnabled(event.getGuild())) return;
        if (!Prime.getMySQL().getVerificationValue(event.getGuild(), "channelid").equals(event.getChannel().getId()))
            return;

        Prime.getMySQL().deleteGuildVerification(event.getGuild());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) return;
        if (!Prime.getMySQL().verificationEnabled(event.getGuild())) return;
        if (event.getUser().isBot())
            return;
        TextChannel channel = event.getGuild().getTextChannelById(Prime.getMySQL().getVerificationValue(event.getGuild(), "channelid"));
        Message message = SafeMessage.sendMessageBlocking(channel, Prime.getMySQL().getVerificationValue(event.getGuild(), "text").replace("%user%", event.getUser().getAsMention()).replace("%guild%", event.getGuild().getName()));
        CommandVerification.users.put(message, event.getUser());

        String emoteRaw = Prime.getMySQL().getVerificationValue(event.getGuild(), "emote");
        if (!StringUtil.isNumeric(emoteRaw))
            message.addReaction(emoteRaw).queue();
        else
            message.addReaction(event.getJDA().getEmoteById(emoteRaw)).queue();
        int delay = Integer.parseInt(Prime.getMySQL().getVerificationValue(event.getGuild(), "kicktime"));
        if (delay == 0) return;
        new VerificationUserHandler.VerifyUser(event.getMember(), message);
        new VerificationKickHandler.VerifyKick(event.getGuild(), event.getMember(), getKickTime(delay), Prime.getMySQL().getVerificationValue(event.getGuild(), "kicktext").replace("%guild%", event.getGuild().getName()), message.getIdLong(), false, true);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (event.getUser().isBot()) return;
        if (VerificationKickHandler.VerifyKick.exists(event.getMember())) {
            VerificationKickHandler.VerifyKick kick = VerificationKickHandler.VerifyKick.fromMember(event.getMember(), true);
            event.getJDA().getTextChannelById(Prime.getMySQL().getVerificationValue(event.getGuild(), "channelid")).getMessageById(kick.getMessageId()).complete().delete().queue();
            kick.remove();
        }
    }


    private Date getKickTime(int mins) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minutes = calendar.get(Calendar.MINUTE) + mins;
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
