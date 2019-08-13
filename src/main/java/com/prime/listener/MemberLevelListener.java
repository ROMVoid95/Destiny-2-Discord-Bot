package com.prime.listener;

import com.prime.sql.MemberSQL;
import com.prime.sql.UserSQL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberLevelListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }
        if (Cooldown.has(event.getAuthor().getId())) {
            return;
        }
        MemberSQL memberSQL = new MemberSQL(event.getMember());
        //Point System
        int currentPoints = Integer.parseInt(memberSQL.get("points"));
        int pRandom = (int) ((Math.random() * 12 + 3));
        int nowPoints = currentPoints + pRandom;
        String sPoints = String.valueOf(nowPoints);
        memberSQL.set("points", sPoints);

        //Cooldown
        Cooldown.add(event.getAuthor().getId());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Cooldown.remove(event.getAuthor().getId());
            }
        }, 20000);

        int currentLevel = Integer.parseInt(memberSQL.get("level"));
        int requiredPoints = getRequiredPointsByLevel(currentLevel);

        if (nowPoints > requiredPoints) {
            currentLevel++;
            String fina = String.valueOf(currentLevel);
            memberSQL.set("level", fina);
            memberSQL.set("points", "0");
            UserSQL userSQL = new UserSQL(event.getAuthor());

        }
    }

    public static int getRequiredPointsByLevel(int level) {
        return (5 * ((level * level / 48) * 49) + 50 * level + 100);
    }

    private static class Cooldown {
        /**
         * Cooldown for MemberLevelListener
         */
        public static ArrayList<String> ids = new ArrayList<>();

        public static boolean has(String id) {
            if (ids.contains(id)) {
                return true;
            } else {
                return false;
            }
        }

        public static void add(String id) {
            ids.add(id);
        }

        public static void remove(String id) {
            ids.remove(id);
        }
    }
}
