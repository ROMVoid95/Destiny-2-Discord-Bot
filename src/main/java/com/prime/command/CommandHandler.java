package com.prime.command;

import static com.prime.util.EmbedUtil.info;
import static com.prime.util.EmbedUtil.message;

import com.prime.Prime;
import com.prime.commands.botowner.CommandMaintenance;
import com.prime.commands.moderation.CommandVerification;
import com.prime.listener.ServerLogHandler;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Colors;
import com.prime.util.EmbedUtil;
import com.prime.util.Info;
import com.prime.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public abstract class CommandHandler {
    private final String[] commandAliase;
    private final CommandCategory category;
    private final PermsNeeded permsNeeded;
    private String description;
    private final String parameterUsage;
    private boolean disabled = false;

    /**
     * Constructs a new CommandHandler.
     *
     * @param commandAliases      the invocation commands (aliases). First entry is the 'main' alias.
     * @param category               the {@link CommandCategory} this command belongs to.
     * @param permsNeeded all permission requirements a user needs to meet to execute a command.
     * @param description            a short command description.
     * @param parameterUsage         the usage message.
     */
    public CommandHandler(String[] commandAliases, CommandCategory category,
                             PermsNeeded permsNeeded, String description, String parameterUsage) {
        this.commandAliase = commandAliases;
        this.category = category;
        this.permsNeeded = permsNeeded;
        this.description = description;
        this.parameterUsage = parameterUsage;
    }

    public CommandHandler(String[] invocationAliases, CommandCategory category,
                             PermsNeeded permissionRequirements, String description, String parameterUsage, boolean disabled) {
        this.commandAliase = invocationAliases;
        this.category = category;
        this.permsNeeded = permissionRequirements;
        this.description = description;
        this.parameterUsage = parameterUsage;
        this.disabled = disabled;
    }

    /**
     * Checks permission, safely calls the execute method and ensures response.
     *
     * @param parsedCommandInvocation the parsed command invocation.
     * @return a response that will be sent and deleted by the caller.
     */
    public Message call(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        if (disabled) {
            return new MessageBuilder().setEmbed(EmbedUtil.info("Command disabled", "Command is currently disabled.").setFooter("TheVoid Dev Team", null).build()).build();
        }
        if (CommandMaintenance.maintenance) {
            ArrayList<Long> authors = new ArrayList<>(Arrays.asList(Info.BOT_AUTHOR_IDS));
            if (!authors.contains(parsedCommandInvocation.getAuthor().getIdLong())) {
                return EmbedUtil.message(EmbedUtil.info("Maintenance!", "Bots maintenance is enabled. Please be patient."));
            }
        }
        UserPermissions userPermissions = new UserPermissions(parsedCommandInvocation.getMessage().getAuthor(),
                parsedCommandInvocation.getMessage().getGuild());
        // check permission
        if (permsNeeded.coveredBy(userPermissions)) {
            // execute command
            try {
                ServerLogHandler.logCommand(parsedCommandInvocation);
                return execute(parsedCommandInvocation, userPermissions);
            } catch (Exception e) { // catch exceptions in command and provide an answer
                Logger.error("Unknown error during the execution of the '" + parsedCommandInvocation.getCommandInvocation() + "' command. ");
                Logger.error(e);
                return new MessageBuilder().setEmbed(new EmbedBuilder()
                        .setAuthor("Error", null, Prime.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("An unknown error occured while executing your command.")
                        .setColor(Colors.COLOR_ERROR)
                        .setFooter(Prime.getNewTimestamp(), null)
                        .build()).build();
            }
        } else
            // respond with 'no-permission'-message
            return new MessageBuilder().setEmbed(new EmbedBuilder()
                    .setAuthor("Missing permissions", null, Prime.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setDescription("You are not permitted to execute this command.")
                    .setColor(Colors.COLOR_NO_PERMISSION)
                    .setFooter(Prime.getNewTimestamp(), null)
                    .build()).build();
    }

    /**
     * Method to be implemented by actual command handlers.
     *
     * @param parsedCommandInvocation the command arguments with prefix and command head removed.
     * @param userPermissions         an object to query the invoker's permissions.
     * @return a response that will be sent and deleted by the caller.
     */
    protected abstract Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions);

    /**
     * @return all aliases this CommandHandler wants to listen to.
     */
    public String[] getCommandAliases() {
        return commandAliase;
    }

    /**
     * @return the category this command belongs to.
     */
    public CommandCategory getCategory() {
        return category;
    }

    /**
     * @return the permission requirements a user needs to meet to execute a command.
     */
    public PermsNeeded getPermsNeeded() {
        return permsNeeded;
    }

    /**
     * @return the short description of this command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of the command
     *
     * @param description the new command description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Use createHelpMessage for a full help message.
     *
     * @return the parameter usage String.
     */
    public String getParameterUsage() {
        return parameterUsage;
    }

    /**
     * Generates a usage message for this command with the default prefix and alias.
     *
     * @return the generated Message.
     */
    public Message createHelpMessage() {
        return createHelpMessage(Info.BOT_DEFAULT_PREFIX, commandAliase[0]);
    }

    /**
     * Generates a usage message for this command.
     *
     * @param invocation data source for prefix and alias to use in the Message.
     * @return the generated Message.
     */
    public Message createHelpMessage(CommandManager.ParsedCommandInvocation invocation) {
        return createHelpMessage(invocation.getPrefix(), invocation.getCommandInvocation());
    }

    /**
     * Generates a usage message for this command.
     *
     * @param serverPrefix which prefix should be used in this message?
     * @param aliasToUse   which alias should be used in this message?
     */
    public Message createHelpMessage(String serverPrefix, String aliasToUse) {
        StringBuilder usage = new StringBuilder();
        for (String part : getParameterUsage().split("\n")) {
            usage.append(serverPrefix + aliasToUse + " " + part + "\n");
        }
        if (this instanceof CommandVerification) {
            if (CommandVerification.showInspired) {
                setDescription("Let your members accept rules before posting messages.");
            } else {
                setDescription("Let your members accept rules before posting messages\n\nThis feature is partially inspired by [Flashbot](https://flashbot.de)");
            }
        }
        return message(info('\'' + aliasToUse + "' command help", getDescription())
                .addField("Aliases", String.join(", ", getCommandAliases()), false)
                .addField("Usage", usage.toString(), false)
                .addField("Permission", permsNeeded.getRequiredPermissionNode(), false));
    }
}
