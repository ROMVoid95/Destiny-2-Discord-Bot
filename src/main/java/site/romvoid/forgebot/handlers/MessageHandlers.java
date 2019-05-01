package site.romvoid.forgebot.handlers;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class MessageHandlers {

//    public void sendPrivateMessage(User target, String message) {
//        sendPrivateMessage(target, message, null);
//    }
//
//    public void sendPrivateMessage(User target, String message, final Consumer<Message> onSuccess) {
//        if (target != null && !target.isFake() && message != null && !message.isEmpty()) {
//            botInstance.queue.add(target.openPrivateChannel(),
//                    privateChannel -> botInstance.queue.add(privateChannel.sendMessage(message), onSuccess));
//        }
//    }
	
    /**
     * Sends a private message to user
     *
     * @param target  the user to send it to
     * @param message the message
     */
    public static void sendPrivateMessage(User user, EmbedBuilder embed) {
    	
        user.openPrivateChannel().queue( (channel) -> channel.sendMessage(embed.build()).queue());
        
    }

}
