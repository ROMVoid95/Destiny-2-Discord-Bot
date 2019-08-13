package com.prime.commands.general;

import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import com.prime.util.Info;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandHelp extends CommandHandler {

    public CommandHelp() {
        super(new String[]{"help", "usage", "?", "command", "manual", "man"}, CommandCategory.GENERAL,
                new PermsNeeded("command.help", false, true),
                "Shows the command manual.", "[command]");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 0) {
            // show complete command manual
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(new MessageBuilder().setEmbed(generateFullHelp(parsedCommandInvocation).build()).build()).queue();
            return null;
        } else {
            CommandHandler handler = Prime.getCommandManager().getCommandHandler(parsedCommandInvocation.getArgs()[0]);
            return handler == null
                    // invalid command
                    ? new MessageBuilder().setEmbed(new EmbedBuilder()
                    .setColor(Colors.COLOR_ERROR)
                    .setTitle(":warning: Invalid command")
                    .setDescription("There is no command named '" + parsedCommandInvocation.getArgs()[0] + "'. Use `"
                            + parsedCommandInvocation.getPrefix() + parsedCommandInvocation.getCommandInvocation()
                            + "` to get a full command list.")
                    .build()).build()
                    // show command help for a single command
                    : handler.createHelpMessage(Info.BOT_DEFAULT_PREFIX, parsedCommandInvocation.getArgs()[0]);
        }
    }

    private EmbedBuilder generateFullHelp(CommandManager.ParsedCommandInvocation invocation) {
        EmbedBuilder builder = new EmbedBuilder();
        List<CommandHandler> filteredCommandList = Prime.getCommandManager().getCommandAssociations().values().stream().filter(commandHandler -> commandHandler.getCategory() != CommandCategory.BOT_OWNER).collect(Collectors.toList());

        ArrayList<String> alreadyAdded = new ArrayList<>();

        StringBuilder listGeneral = new StringBuilder();
        for (CommandHandler commandHandler : filteredCommandList) {
            if (commandHandler.getCategory() == CommandCategory.GENERAL && !alreadyAdded.contains(commandHandler.getCommandAliases()[0])) {
                alreadyAdded.add(commandHandler.getCommandAliases()[0]);
                listGeneral.append("`").append(commandHandler.getCommandAliases()[0]).append("` ");
            }
        }
        StringBuilder listModeration = new StringBuilder();
        for (CommandHandler commandHandler : filteredCommandList) {
            if (commandHandler.getCategory() == CommandCategory.MODERATION && !alreadyAdded.contains(commandHandler.getCommandAliases()[0])) {
                alreadyAdded.add(commandHandler.getCommandAliases()[0]);
                listModeration.append("`").append(commandHandler.getCommandAliases()[0]).append("` ");
            }
        }
        StringBuilder listAdmin = new StringBuilder();
        for (CommandHandler commandHandler : filteredCommandList) {
            if (commandHandler.getCategory() == CommandCategory.ADMIN && !alreadyAdded.contains(commandHandler.getCommandAliases()[0])) {
                alreadyAdded.add(commandHandler.getCommandAliases()[0]);
                listAdmin.append("`").append(commandHandler.getCommandAliases()[0]).append("` ");
            }
        }
        StringBuilder listSettings = new StringBuilder();
        for (CommandHandler commandHandler : filteredCommandList) {
            if (commandHandler.getCategory() == CommandCategory.SETTINGS && !alreadyAdded.contains(commandHandler.getCommandAliases()[0])) {
                alreadyAdded.add(commandHandler.getCommandAliases()[0]);
                listSettings.append("`").append(commandHandler.getCommandAliases()[0]).append("` ");
            }
        }
        StringBuilder listDestiny = new StringBuilder();
        for (CommandHandler commandHandler : filteredCommandList) {
            if (commandHandler.getCategory() == CommandCategory.DESTINY && !alreadyAdded.contains(commandHandler.getCommandAliases()[0])) {
                alreadyAdded.add(commandHandler.getCommandAliases()[0]);
                listDestiny.append("`").append(commandHandler.getCommandAliases()[0]).append("` ");
            }
        }

        builder.setTitle(":information_source: PrimeBot command manual");
        builder.setDescription("Use `" + invocation.getPrefix() + "help <command>` to get a more information about a command.");
        builder.setColor(Colors.COLOR_SECONDARY);
        builder.setFooter("Loaded a total of "
                + new HashSet<>(Prime.getCommandManager().getCommandAssociations().values()).size()
                + " commands.", null);

        //Add Categories
        builder.addField("General", listGeneral.toString(), false);
        builder.addField("Destiny", listDestiny.toString(), false);
        builder.addField("Settings", listSettings.toString(), false);
        builder.addField("Moderation", listModeration.toString(), false);
        return builder;
    }
}
