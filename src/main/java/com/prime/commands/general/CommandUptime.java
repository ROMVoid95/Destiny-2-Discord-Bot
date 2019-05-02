package com.prime.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Info;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandUptime extends CommandHandler {

    public CommandUptime() {
        super(new String[]{"uptime"}, CommandCategory.GENERAL, new PermsNeeded("command.uptime", false, true), "Get the Uptime of the Bot", "", false);
    }

    private String getTime(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private String getTimeDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays + " d, " + parseTimeNumbs(diffHours) + " h, " + parseTimeNumbs(diffMinutes) + " min, " + parseTimeNumbs(diffSeconds) + " sec";
    }

    private String parseTimeNumbs(long time) {
        String timeString = time + "";
        if (timeString.length() < 2)
            timeString = "0" + time;
        return timeString;
    }


    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        parsedCommandInvocation.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(new Color(255, 71, 0))
                        .setDescription(":alarm_clock:   **UPTIME**")
                        .addField("Last restart", getTime(Info.lastRestart, "dd.MM.yyyy - HH:mm:ss (z)"), false)
                        .addField("Online since", getTimeDiff(new Date(), Info.lastRestart), false)
                        .build()
        ).queue();
        return null;
    }
}
