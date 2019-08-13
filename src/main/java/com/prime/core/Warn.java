package com.prime.core;

import com.prime.Prime;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class Warn {

    private final int id;
    private final User warnedUser;
    private final Guild guild;
    private final User executor;
    private final String reason;
    private final long date;

    public Warn(int id, User warnedUser, Guild guild, User executor, String reason, long date) {
        this.id = id;
        this.warnedUser = warnedUser;
        this.guild = guild;
        this.executor = executor;
        this.reason = reason;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public User getWarnedUser() {
        return warnedUser;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getExecutor() {
        return executor;
    }

    public String getReason() {
        return reason;
    }

    public long getDate() {
        return date;
    }

    public static Warn parseWarn(String id, String warnedUser, String guildid, String executor, String reason, String date) {
        int pId = Integer.parseInt(id);
        User pWarnedUser = Prime.getJDA().getUserById(warnedUser);
        Guild guild = Prime.getJDA().getGuildById(guildid);
        User pExecutor = Prime.getJDA().getUserById(executor);
        long pDate = Long.valueOf(date);
        return new Warn(pId, pWarnedUser, guild, pExecutor, reason, pDate);
    }
}
