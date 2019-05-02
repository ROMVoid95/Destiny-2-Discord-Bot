package com.prime.commands.botowner;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import static com.prime.util.EmbedUtil.info;
import static com.prime.util.EmbedUtil.message;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import java.util.List;

public class CommandGuilds extends CommandHandler {
    public CommandGuilds() {
        super(new String[]{"guilds"}, CommandCategory.BOT_OWNER,
                new PermsNeeded("command.guilds", true, false),
                "Shows all Guilds the Bot is running on!", "guilds");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        StringBuilder runningOnServers = new StringBuilder();
        int count_server = 1;

        List<Guild> guild_sublist;
        int SideNumbInput = 1;
        if (parsedCommandInvocation.getArgs().length > 0) {
            SideNumbInput = Integer.parseInt(parsedCommandInvocation.getArgs()[0]);
            System.out.println(SideNumbInput);
        }

        if (Prime.getJDA().getGuilds().size() > 20) {
            guild_sublist = Prime.getJDA().getGuilds().subList((SideNumbInput - 1) * 20, (SideNumbInput - 1) * 20 + 20);
        } else {
            guild_sublist = Prime.getJDA().getGuilds();
        }


        int sideNumbAll;
        if (Prime.getJDA().getGuilds().size() >= 20) {
            for (Guild guild : guild_sublist) {
                runningOnServers.append("`\t " + (((SideNumbInput - 1) * 20) + count_server) + ". ").append(guild.getName()).append("(").append(guild.getId()).append(")`\n");
                count_server++;
            }
            sideNumbAll = Prime.getJDA().getGuilds().size() / 20;
        } else {
            for (Guild guild : guild_sublist) {
                runningOnServers.append("`\t " + count_server + ". ").append(guild.getName()).append("(").append(guild.getId()).append(")`\n");
                count_server++;
            }
            sideNumbAll = 1;
        }
        int sideNumb = SideNumbInput;
        return message(info("TheVoid running on following guilds", "`Total guilds: " + Prime.getJDA().getGuilds().size() + " - Side " + sideNumb + " / " + sideNumbAll + "`\n\n" + runningOnServers.toString()));
    }
}
