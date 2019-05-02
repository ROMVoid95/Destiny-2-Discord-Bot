package com.prime.commands.botowner;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CommandGuildData extends CommandHandler {

    public CommandGuildData() {
        super(new String[]{"guild-data"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.guilddata", true, false), "Saves guild data in a file. Only works on TheVoid Server.", "");
    }

    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getGuild().getIdLong() != 538530739017220107L)
            return null;
        new Thread(() -> {
            StringBuilder builder = new StringBuilder();
            for (Guild guild : Prime.getJDA().getGuilds()) {
                builder.append(guild.getName() + "(" + guild.getId() + ") [" + guild.getMembers().size() + "]\n");
            }
            File tempFile = new File("data/temp", "guilddata.txt");
            try {
                tempFile.createNewFile();
                FileWriter writer = new FileWriter(tempFile);
                writer.write(builder.toString());
                writer.flush();
                writer.close();
                parsedCommandInvocation.getTextChannel().sendFile(tempFile).complete().delete().queueAfter(20, TimeUnit.SECONDS);
                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return null;
    }
}
