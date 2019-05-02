package com.prime.listener;

import com.prime.core.GameAnimator;
import com.prime.util.Info;
import java.io.IOException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class readyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        StringBuilder sb = new StringBuilder();
        event.getJDA().getGuilds()
                .forEach(guild -> sb.append("|  - \"" + guild.getName() + "\" - \n| \t ID:    "
                        + guild.getId() + " \n| \t Owner: " + guild.getOwner().getUser().getName()
                        + "#" + guild.getOwner().getUser().getDiscriminator() + " \n|\n"));

        System.out.println(String.format("\n\n"
                + "#--------------------------------------------------------------------------------\n"
                + "| %s - v.%s (JDA: v.%s)\n"
                + "#--------------------------------------------------------------------------------\n"
                + "| Running on %s guilds: \n" + "%s"
                + "#--------------------------------------------------------------------------------\n\n",
                 "PrimeBot", Info.BOT_VERSION, "3.8.2_459",
                event.getJDA().getGuilds().size(), sb.toString()));

        try {
        	ReactionListener.loadRoles(event.getJDA());
            for (TextChannel tc : ReactionListener.mappedRoles.keySet()) {
                for (Message m : ReactionListener.mappedRoles.get(tc).keySet()) {
                    for (Emote e : ReactionListener.mappedRoles.get(tc).get(m).keySet())
                        m.addReaction(e).queue();;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
			GameAnimator.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
