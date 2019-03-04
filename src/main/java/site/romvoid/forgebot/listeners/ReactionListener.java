package site.romvoid.forgebot.listeners;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import site.romvoid.forgebot.util.Config;
import site.romvoid.forgebot.util.RoleAssignment;

public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        TextChannel channel = event.getTextChannel();
        Message message = null;
        Emote emote = event.getReactionEmote().getEmote();
        GuildController g = event.getGuild().getController();
        Role questionable = event.getGuild().getRoleById("449561870970519562");
        Role regions = event.getGuild().getRoleById("531873048660541471");
        Member member = event.getMember();
        User u = event.getUser();
        

        if (event.getUser().isBot())
            return;
        if (!Config.mappedRoles.keySet().contains(channel))
            return;
        for (Message m : Config.mappedRoles.get(channel).keySet()) {
            if (m.getId().equals(event.getMessageId())) {
                message = m;
            }
        }
        g.addSingleRoleToMember(member, Config.mappedRoles.get(channel).get(message).get(emote)).queue();
        g.addSingleRoleToMember(member, regions).queue();
        g.removeSingleRoleFromMember(event.getMember(), questionable).queue();
            event.getReaction().removeReaction(u).queue();
            try {
                RoleAssignment.action(event);
            } catch (Exception e) {
                e.printStackTrace();
        }
    }
}
