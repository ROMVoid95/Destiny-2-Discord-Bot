package site.romvoid.forgebot.listeners;

import java.awt.Color;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import site.romvoid.forgebot.util.MySQL;
import site.romvoid.forgebot.util.embedSender;

public class MentionListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if(message.getMentionedUsers().size() > 0){
            if (message.getMentionedUsers().get(0).equals(event.getGuild().getSelfMember().getUser())){
                MessageChannel channel = event.getChannel();
                channel.sendTyping().queue();
                embedSender.sendPermanentEmbed("**[ForgeBot]** \n **My Prefix** `" + MySQL.getValue(event.getGuild(), "prefix") + "` \n:question: **Help** `" + MySQL.getValue(event.getGuild(), "prefix") + "help`", channel, Color.cyan);
            }
        }
    }
}
