package com.prime.listener;

import com.prime.Prime;
import com.prime.util.Logger;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


/**
 * Listen if the PrimeBot leaves a guild
 */
public class BotLeaveListener extends ListenerAdapter {

    /**
     * Removes the guild from the database
     *
     * @param e
     */
    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        try {
            if (Prime.getMySQL().ifGuildExits(e.getGuild())) {
                Prime.getMySQL().deleteGuild(e.getGuild());
            }
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }
}


