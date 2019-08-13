/*
 * Copyright (c) 2017 PrimeBot Bot Development Team
 *
 * Licensed under the MIT license. The full license text is available in the LICENSE file provided with this project.
 */

package com.prime.command;

import com.prime.Prime;
import com.prime.util.DevCommandLog;
import com.prime.util.EmbedUtil;
import com.prime.util.GlobalBlacklist;
import com.prime.util.Info;
import com.prime.util.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandManager extends ListenerAdapter {
    private final Map<String, CommandHandler> commandAssociations = new HashMap<>();

    /**
     * Constructs and registers the command manager.
     */
    public CommandManager() {
        Prime.registerEventListener(this);
    }

    /**
     * Registers multiple CommandHandlers with their invocation aliases.
     *
     * @param commandHandlers the CommandHandlers to register.
     */
    public void registerCommandHandlers(CommandHandler... commandHandlers) {
        for (CommandHandler commandHandler : commandHandlers)
            registerCommandHandler(commandHandler);
    }

    /**
     * Registers a CommandHandler with it's invocation aliases.
     *
     * @param commandHandler the {@link CommandHandler} to be registered.
     */
    public void registerCommandHandler(CommandHandler commandHandler) {
        for (String invokeAlias : commandHandler.getCommandAliases())
            // only register if alias is not taken
            if (commandAssociations.containsKey(invokeAlias.toLowerCase()))
                Logger.warning("The '" + commandHandler.toString()
                        + "' CommandHandler tried to register the alias '" + invokeAlias
                        + "' which is already taken by the '" + commandAssociations.get(invokeAlias).toString()
                        + "' CommandHandler.");
            else
                commandAssociations.put(invokeAlias.toLowerCase(), commandHandler);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (event.isFromType(ChannelType.PRIVATE)) return;
//        GuildSQL guildSQL = GuildSQL.fromGuild(event.getGuild());
//        if (guildSQL.enabledBlacklist())
//            if (guildSQL.isBlacklisted(event.getTextChannel())) return;
//             if (guildSQL.enabledWhitelist())
//                if (!guildSQL.isWhitelisted(event.getTextChannel())) return;

        super.onMessageReceived(event);
        ParsedCommandInvocation commandInvocation = parse(event.getMessage());
        //Send typing because it's useless
        if (commandInvocation != null && !event.getAuthor().isBot() && !event.getAuthor().isFake() && !event.isWebhookMessage()) {
            if (GlobalBlacklist.isOnBlacklist(event.getAuthor())) {
                event.getTextChannel().sendMessage(EmbedUtil.message(EmbedUtil.error("Blacklisted", "You are blacklisted from using TheVoid! ;)"))).queue(msg -> msg.delete().queueAfter(20, TimeUnit.SECONDS));
                return;
            }
            call(commandInvocation);
        }
    }

    /**
     * Call the CommandHandler for commandInvocation.
     *
     * @param parsedCommandInvocation the parsed message.
     */
    private void call(ParsedCommandInvocation parsedCommandInvocation) {
        CommandHandler commandHandler = getCommandHandler(parsedCommandInvocation.getCommandInvocation());
        Message response;
        if (commandHandler == null) {
            /*response = EmbedUtil.message(EmbedUtil.withTimestamp(EmbedUtil.error("Unknown command", "'" + parsedCommandInvocation.serverPrefix + parsedCommandInvocation.invocationCommand
                    + "' could not be resolved to a command.\nType '" + parsedCommandInvocation.serverPrefix
                    + "help' to get a list of all commands.")));*/
            return;
        } else {
            DevCommandLog.log(parsedCommandInvocation);
            response = commandHandler.call(parsedCommandInvocation);
        }

        // respond
        if (response != null)
            EmbedUtil.sendAndDeleteOnGuilds(parsedCommandInvocation.getMessage().getChannel(), response);

        // delete invocation message
        if (parsedCommandInvocation.getGuild() != null) {
            if (!parsedCommandInvocation.getGuild().getSelfMember().getPermissions(parsedCommandInvocation.getTextChannel()).contains(Permission.MESSAGE_MANAGE))
                return; // Do not try to delete message when bot is not allowed to
            parsedCommandInvocation.getMessage().delete().queue(null, msg -> {
            }); // suppress failure
        }
    }

    /**
     * Parses a raw message into command components.
     *
     * @param message the discord message to parse.
     * @return a {@link ParsedCommandInvocation} with the parsed arguments or null if the message could not be
     * resolved to a command.
     */
    private static ParsedCommandInvocation parse(Message message) {
        String prefix = null;
        // react to mention: '@botmention<majorcommand> [arguments]'
        if (message.getContentRaw().startsWith(Prime.getJDA().getSelfUser().getAsMention())) {
            prefix = Prime.getJDA().getSelfUser().getAsMention();
            // react to default prefix: '>>><majorcommand> [arguments]'
        } else if (message.getContentRaw().toLowerCase().startsWith(Info.BOT_DEFAULT_PREFIX.toLowerCase())) {
            prefix = message.getContentRaw().substring(0, Info.BOT_DEFAULT_PREFIX.length());
        }
        // react to custom server prefix: '<custom-server-prefix><majorcommand> [arguments...]'
        else if (message.getChannelType() == ChannelType.TEXT) { // ensure bot is on a server
            String serverPrefix = Prime.getMySQL().getGuildValue(message.getGuild(), "prefix");
            if (message.getContentRaw().toLowerCase().startsWith(serverPrefix.toLowerCase()))
                prefix = serverPrefix;
        }

        if (prefix != null) {
            // cut off command prefix
            String beheaded = message.getContentRaw().substring(prefix.length(), message.getContentRaw().length()).trim();
            // split arguments
            String[] allArgs = beheaded.split("\\s+");
            // create an array of the actual command arguments (exclude invocation arg)
            String[] args = new String[allArgs.length - 1];
            System.arraycopy(allArgs, 1, args, 0, args.length);
            return new ParsedCommandInvocation(message, prefix, allArgs[0], args);
        }
        // else
        return null; // = message is not a command
    }

    /**
     * @param invocationAlias the key property to the CommandHandler.
     * @return the associated CommandHandler or null if none is associated.
     */
    public CommandHandler getCommandHandler(String invocationAlias) {
        return commandAssociations.get(invocationAlias.toLowerCase());
    }

    /**
     * @return a clone of all registered command associations.
     */
    public Map<String, CommandHandler> getCommandAssociations() {
        return new HashMap<>(commandAssociations);
    }

    public static final class ParsedCommandInvocation {

        private final String[] argsNew;
        private final String commandInvocation;
        private final Message message;
        private final String prefix;

        private ParsedCommandInvocation(Message invocationMessage, String serverPrefix, String invocationCommand, String[] args) {
            this.message = invocationMessage;
            this.prefix = serverPrefix;
            this.commandInvocation = invocationCommand;
            this.argsNew = args;
        }

        public Message getMessage() {
            return message;
        }

        public Guild getGuild() {
            return message.getGuild();
        }

        public String[] getArgs() {
            return argsNew;
        }

        public String getCommandInvocation() {
            return commandInvocation;
        }

        public String getPrefix() {
            return prefix;
        }

        public Member getSelfMember() {
            return message.getGuild().getSelfMember();
        }

        public User getAuthor() {
            return message.getAuthor();
        }

        public Member getMember() {
            return message.getMember();
        }

        public TextChannel getTextChannel() {
            return message.getTextChannel();
        }
    }
}
