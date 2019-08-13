package com.prime.listener;

import com.prime.Prime;
import com.prime.sql.ServerLogSQL;
import com.prime.util.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * If the PrimeBot joins a new guild
 */
public class BotJoinListener extends ListenerAdapter {

    /**
     * Creates the new guild in the database
     *
     * @param event
     */
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        try {
            Guild g = event.getGuild();
            if (!Prime.getMySQL().ifGuildExits(event.getGuild())) {
                Prime.getMySQL().createGuildServer(g);
                new ServerLogSQL(event.getGuild());
            }
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }
}