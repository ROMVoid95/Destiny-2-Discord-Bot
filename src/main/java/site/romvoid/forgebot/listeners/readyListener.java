package site.romvoid.forgebot.listeners;

import java.io.IOException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import site.romvoid.forgebot.util.Config;
import site.romvoid.forgebot.util.Logger;
import site.romvoid.forgebot.util.STATIC;


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
                Logger.Cyan + Logger.Bold + "VoidBot" + Logger.Reset, STATIC.VERSION, "3.8.2_459",
                event.getJDA().getGuilds().size(), sb.toString()));

        try {
            Config.loadRoles(event.getJDA());
            for (TextChannel tc : Config.mappedRoles.keySet()) {
                for (Message m : Config.mappedRoles.get(tc).keySet()) {
                    for (Emote e : Config.mappedRoles.get(tc).get(m).keySet())
                        m.addReaction(e).queue();;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
