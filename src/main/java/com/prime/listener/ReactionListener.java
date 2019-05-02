package com.prime.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.prime.util.RoleAssignment;
import com.prime.util.embedSender;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class ReactionListener extends ListenerAdapter {
	
	private static final File configYML = new File("config.yml");
	private static final ObjectMapper configMapper = new ObjectMapper(new YAMLFactory());
	private static ConfigRoles LoadedRoles;
	public static Map<TextChannel, Map<Message, Map<Emote, Role>>> mappedRoles = new HashMap<>();

	public static void loadRoles(JDA jda) throws IOException {
		LoadedRoles = configMapper.readValue(configYML, ConfigRoles.class);

		for (String channel : LoadedRoles.selfRole.keySet()) {
			for (String message : LoadedRoles.selfRole.get(channel).keySet()) {
				Map<Message, Map<Emote, Role>> messageMapMap = new HashMap<>();
				Message msg = jda.getTextChannelById(channel).getMessageById(message).complete();
				{
					Map<Emote, Role> roleEmoteMap = new HashMap<>();
					for (String role : LoadedRoles.selfRole.get(channel).get(message).keySet()) {
						roleEmoteMap.put(jda.getEmoteById(LoadedRoles.selfRole.get(channel).get(message).get(role)), jda.getRoleById(role));
					}
					messageMapMap.put(msg, roleEmoteMap);
				}
				mappedRoles.put(jda.getTextChannelById(channel), messageMapMap);
			}
		}
	}
	
	private static class ConfigRoles {
		public Map<String, Map<String, Map<String, String>>> selfRole;
	}
	
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
		for (Message m : mappedRoles.get(channel).keySet()) {
			if (m.getId().equals(event.getMessageId())) {
				message = m;
			}
		}
		if (event.getUser().isBot()) return;
		if (event.getMember().getNickname() == null) {
			embedSender.nicknameExcemption(event.getUser(), event.getMember().getEffectiveName());
			event.getReaction().removeReaction(u).queue();
			return;
		} else {
			g.addSingleRoleToMember(member, mappedRoles.get(channel).get(message).get(emote)).queue();
			g.addSingleRoleToMember(member, regions).queue();
			g.removeSingleRoleFromMember(event.getMember(), questionable).queue();
			event.getReaction().removeReaction(u).queue();
			RoleAssignment.action(event);
		}
	}

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

    }
}
