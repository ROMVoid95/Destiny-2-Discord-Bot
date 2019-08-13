package com.prime.listener;

import com.prime.Prime;
import com.prime.util.Colors;
import com.prime.util.Info;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SelfMentionListener extends ListenerAdapter {

    private final String[] EMOJIS = {"\uD83C\uDDF7", "\uD83C\uDDFA", "\uD83C\uDDE7", "\uD83C\uDDEE", "\uD83C\uDDE8", "\uD83C\uDDF4", "\uD83C\uDDF3"};

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getMessage().getMentionedUsers().contains(e.getJDA().getSelfUser())) {
            if (!e.getMessage().getContentDisplay().replaceFirst("@", "").equals(e.getGuild().getSelfMember().getEffectiveName()))
                return;
            Message message = e.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Colors.COLOR_SECONDARY)
                            .setAuthor(e.getJDA().getSelfUser().getName(), null, e.getJDA().getSelfUser().getAvatarUrl())
                            .setDescription("Hello Guardian, my name is " + Info.BOT_NAME)
                            .addField("**Prefix**", "`" + Prime.getMySQL().getGuildValue(e.getGuild(), "prefix") + "`", false)
                            .addField("**Documentation**", "[GitHub](https://github.com/ROMVoid95/Destiny-2-Discord-Bot)", false)
                            .build()
            ).complete();
            for (String emoji : EMOJIS) {
                message.addReaction(emoji).queue();
            }
            if (!e.getGuild().getSelfMember().getPermissions(e.getChannel()).contains(Permission.MESSAGE_MANAGE))
                return; // Do not try to delete message when bot is not allowed to
            message.delete().queueAfter(5, TimeUnit.MINUTES);
            e.getMessage().delete().queue();
        }
    }

}
