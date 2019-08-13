package com.prime.commands.botowner;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.sql.MySQL;
import java.util.Timer;
import java.util.TimerTask;

public class CommandRestart extends CommandHandler {
    public CommandRestart() {
        super(new String[]{"rs", "restart", "r"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.restart", true, false), "Restart the Bot!", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        MySQL sql = Prime.getMySQL();
        sql.disconnect();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Prime.getJDA().shutdown();
            }
        }, 20000);
        Prime.initJDA();
        sql.connect();
        return new MessageBuilder().setEmbed(new EmbedBuilder().setDescription(":battery: Bot & JVM Restarted :battery:").build()).build();
    }
}
