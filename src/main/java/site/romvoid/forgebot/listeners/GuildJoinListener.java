package site.romvoid.forgebot.listeners;

import java.awt.Color;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import site.romvoid.forgebot.util.MySQL;
import site.romvoid.forgebot.util.embedSender;

public class GuildJoinListener extends ListenerAdapter{

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if(!MySQL.ifGuildExists(event.getGuild()))
            MySQL.createServer(event.getGuild());
        MessageChannel channel = event.getGuild().getDefaultChannel();
        channel.sendTyping().queue();
        embedSender.sendPermanentEmbed("**[ForgeBot]** \n **My Prefix** `" + MySQL.getValue(event.getGuild(), "prefix") + "` \n:question: **Help** `" + MySQL.getValue(event.getGuild(), "prefix") + "help`", channel, Color.cyan);

    }
}
