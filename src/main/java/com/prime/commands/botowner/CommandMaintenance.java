package com.prime.commands.botowner;

import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import com.prime.util.EmbedUtil;
import com.prime.util.SafeMessage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

public class CommandMaintenance extends CommandHandler {

    public static boolean maintenance = false;


    public CommandMaintenance() {
        super(new String[]{"maintenance", "wartung"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.maintenance", true, false), "Starts bot maintenance.", "<message for playing status>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 1) {
            if (parsedCommandInvocation.getArgs()[0].equalsIgnoreCase("false")) {
                disable();
                return EmbedUtil.message(EmbedUtil.success("Disabled maintenance", "Successfully disabled maintenance"));
            }
        }
        if (parsedCommandInvocation.getArgs().length < 1)
            return createHelpMessage();

        //Enabling with MaintenanceCommand
        Prime.getConfiguration().set("maintenance", "1");
        //Build Status message
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < parsedCommandInvocation.getArgs().length; i++) {
            msg.append(parsedCommandInvocation.getArgs()[i]).append(" ");
        }
        //Set playing status
        Prime.getConfiguration().set("playingStatus", msg.toString());
        enable();
        SafeMessage.sendMessage(parsedCommandInvocation.getTextChannel(), new EmbedBuilder().setColor(Colors.COLOR_PRIMARY).setTitle("Activated Maintenance").setAuthor(parsedCommandInvocation.getAuthor().getName(), null, parsedCommandInvocation.getAuthor().getEffectiveAvatarUrl()).setDescription("Bot will only respond to owners.").build());
        return null;
    }

    public static void disable() {
        maintenance = false;
        Prime.getConfiguration().set("playingStatus", "0");
        Prime.getConfiguration().set("maintenance", "0");
        Prime.getJDA().getPresence().setGame(Game.playing("Maintenance is over"));
        Prime.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
    }

    public static void enable() {
        maintenance = true;
        Prime.getJDA().getPresence().setGame(Game.playing(Prime.getConfiguration().getString("playingStatus")));
        Prime.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
    }
}

