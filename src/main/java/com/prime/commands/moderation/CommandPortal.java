package com.prime.commands.moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandPortal extends CommandHandler {

    private String portalChannelName = "thevoid-portal";
    private String closedChannelName = "closed-thevoid-portal";
    private File inviteFile = new File(Prime.getDataFolder() + "portal-invites.json");

    public CommandPortal() {
        super(new String[]{"portal", "mirror", "phone"}, CommandCategory.MODERATION, new PermsNeeded("command.portal", false, false), "Create a portal and talk with users from other guilds.", "create\nclose\ninvite <serverid>\naccept <serverid>\ninfo");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 0) {
            return createHelpMessage(parsedCommandInvocation);
        }

        switch (parsedCommandInvocation.getArgs()[0].toLowerCase()) {
            case "open":
            case "o":
            case "create":
                if (parsedCommandInvocation.getArgs().length == 1) {
                    createPortalWithRandomGuild(parsedCommandInvocation);
                    return null;
                }
                return null;
            case "close":
                closePortal(parsedCommandInvocation);
                return null;
            case "invite":
                if (parsedCommandInvocation.getArgs().length == 2) {
                    inviteGuild(parsedCommandInvocation);
                } else {
                    return createHelpMessage();
                }
                return null;
            case "accept":
                if (parsedCommandInvocation.getArgs().length == 2) {
                    acceptInvite(parsedCommandInvocation);
                } else {
                    createHelpMessage();
                }
                return null;
            case "info":
                portalInfo(parsedCommandInvocation);
                return null;
            default:
                return createHelpMessage(parsedCommandInvocation);
        }
    }

    private void portalInfo(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        String entry = Prime.getMySQL().getGuildValue(parsedCommandInvocation.getGuild(), "portal");
        TextChannel channel = parsedCommandInvocation.getTextChannel();
        if(entry.equals("open")){
            Guild guild = Prime.getJDA().getGuildById(Prime.getMySQL().getPortalValue(parsedCommandInvocation.getGuild(), "partnerid"));
            SafeMessage.sendMessage(channel, EmbedUtil.info("Connected to " + guild.getName(), "This server is currently connected to `" + guild.getName() + "`!").build(), 8);
        } else if (entry.equals("waiting")){
            SafeMessage.sendMessage(channel, EmbedUtil.error("Portal is waitng", "The portal is currently waiting to be accepted by the other side").build(), 5);
        } else if(entry.equals("closed")){
            SafeMessage.sendMessage(channel, EmbedUtil.error("Portal is closed", "The portal is currently closed").build(), 5);
        }

    }

    /**
     * Creates a portal with a random guild
     *
     * @param parsedCommandInvocation parsedCommandInvocation
     */
    private void createPortalWithRandomGuild(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        Guild messageGuild = parsedCommandInvocation.getMessage().getGuild();
        TextChannel messageChannel = parsedCommandInvocation.getMessage().getTextChannel();

        //Check if portal exists
        String oldGuildPortalEntry = Prime.getMySQL().getGuildValue(messageGuild, "portal");
        if (oldGuildPortalEntry.equals("open") || oldGuildPortalEntry.contains("waiting") || Prime.getMySQL().ifPortalExist(messageGuild)) {
            messageChannel.sendMessage(EmbedUtil.error("Portal error!", "Portal is already open.").build()).queue();
            return;
        }

        List<Guild> waitingGuilds = Prime.getMySQL().getGuildsByValue("portal", "waiting");
        if (waitingGuilds.size() == 0) {
            setGuildWaiting(messageGuild, messageChannel);
        } else {
            connectGuilds(messageGuild, waitingGuilds.get(0), messageChannel);
        }
    }

    private void inviteGuild(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        Guild messageGuild = parsedCommandInvocation.getMessage().getGuild();
        TextChannel messageChannel = parsedCommandInvocation.getMessage().getTextChannel();
        if (!StringUtil.isNumeric(parsedCommandInvocation.getArgs()[1])) {
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.error("Portal error", "Invalid guild id.").build()).queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        if (parsedCommandInvocation.getArgs()[1].equalsIgnoreCase(messageGuild.getId())) {
            messageChannel.sendMessage(EmbedUtil.error("Portal error!", "Portal you can't invite yourself.").build()).queue();
            return;
        }

        //Check if portal exists
        String oldGuildPortalEntry = Prime.getMySQL().getGuildValue(messageGuild, "portal");
        if (oldGuildPortalEntry.equals("open") || oldGuildPortalEntry.contains("waiting")) {
            messageChannel.sendMessage(EmbedUtil.error("Portal error!", "Portal is already open.").build()).queue();
            return;
        }
        String guildId = parsedCommandInvocation.getArgs()[1];
        Guild guildTwo = null;
        try {
            guildTwo = parsedCommandInvocation.getMessage().getJDA().getGuildById(guildId);
        } catch (NullPointerException ignored) {
            // Guild doesn't exist
        }

        if (guildTwo == null) {
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.error("Portal error", "Invalid guild id.").build()).queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        if (Prime.getMySQL().getGuildValue(guildTwo, "portal").equals("waiting") || Prime.getMySQL().getGuildValue(guildTwo, "portal").equals("closed")) {
            EmbedBuilder builder = EmbedUtil.embed("Portal Invite", parsedCommandInvocation.getMessage().getGuild().getName() + "(" + parsedCommandInvocation.getMessage().getGuild().getId() + ") has sent you an portal invite.\n`Accept with >>>portal accept <serverid>`");
            builder.setFooter("Execute this command on your server", null);
            builder.setColor(Colors.COLOR_PRIMARY);
            guildTwo.getOwner().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(builder.build()).queue());

            if (!inviteFile.exists()) {
                try {
                    inviteFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Configuration configuration = new Configuration(inviteFile);
            createInviteEntryIfNotExists(configuration, guildTwo);
            configuration.set(guildTwo.getId() + "." + messageGuild.getId(), 1);
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.success("Portal Invite sent", "Successfully sent an portal invite to " + guildTwo.getName()).build()).queue();
        }
    }

    private void acceptInvite(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        Guild guildOne = parsedCommandInvocation.getMessage().getGuild();
        if (guildOne == null) {
            parsedCommandInvocation.getMessage().getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(EmbedUtil.error("Portal error!", "You only can execute this command on your server.").build()).queue());
            return;
        }
        Guild guildTwo;

        if (!StringUtil.isNumeric(parsedCommandInvocation.getArgs()[1])) {
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.error("Portal error", "Invalid guild id!").build()).queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        try {
            guildTwo = parsedCommandInvocation.getMessage().getJDA().getGuildById(parsedCommandInvocation.getArgs()[1]);
        } catch (NullPointerException ex) {
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.error("Portal error", "Invalid guild id!").build()).queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        if (guildTwo == null) {
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.error("Portal error", "Invalid guild id!").build()).queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        Configuration configuration = new Configuration(inviteFile);
        createInviteEntryIfNotExists(configuration, guildTwo);

        if (configuration.has(guildOne.getId() + "." + guildTwo.getId())) {
            connectGuilds(guildOne, guildTwo, parsedCommandInvocation.getMessage().getTextChannel());
            configuration.set(guildOne.getId() + "." + guildTwo.getId(), null);
        } else {
            parsedCommandInvocation.getMessage().getTextChannel().sendMessage(EmbedUtil.error("Portal error!", "You don't have an invite from this guild!").build()).queue();
        }
    }

    /**
     * Create portals at two guilds
     *
     * @param guildOne message guild
     * @param guildTwo guild with waiting status
     */
    private void connectGuilds(Guild guildOne, Guild guildTwo, TextChannel messageChannel) {
        //Channel creation and waiting check
        try {
            TextChannel channelOne;
            TextChannel channelTwo;
            if (guildOne.getTextChannelsByName(portalChannelName, true).size() == 0) {
                if (guildOne.getMemberById(Prime.getJDA().getSelfUser().getId()).getPermissions().contains(Permission.MANAGE_CHANNEL)) {
                    channelOne = (TextChannel) guildOne.getController().createTextChannel(portalChannelName).complete();
                } else {
                    messageChannel.sendMessage(EmbedUtil.error("Portal Error!", "I need the `MANAGE_CHANNEL` permissions or you create yourself a channel called `rubicon-portal`.").setFooter(guildOne.getName(), null).build()).queue();
                    return;
                }
            } else {
                channelOne = guildOne.getTextChannelsByName(portalChannelName, true).get(0);
            }
            if (guildTwo.getTextChannelsByName(portalChannelName, true).size() == 0) {
                if (guildTwo.getMemberById(Prime.getJDA().getSelfUser().getId()).getPermissions().contains(Permission.MANAGE_CHANNEL)) {
                    channelTwo = (TextChannel) guildTwo.getController().createTextChannel(portalChannelName).complete();
                } else {
                    guildTwo.getOwner().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(EmbedUtil.error("Portal Error!", "I need the `MANAGE_CHANNEL` permissions or you create yourself a channel called `rubicon-portal`.").setFooter(guildTwo.getName(), null).build()).queue());
                    Prime.getMySQL().updateGuildValue(guildTwo, "portal", "closed");
                    setGuildWaiting(guildOne, messageChannel);
                    return;
                }
            } else {
                channelTwo = guildTwo.getTextChannelsByName(portalChannelName, true).get(0);
            }
            //Update Database Values
            Prime.getMySQL().updateGuildValue(guildOne, "portal", "open");
            Prime.getMySQL().createPortal(guildOne, guildTwo, channelOne);
            Prime.getMySQL().updateGuildValue(guildTwo, "portal", "open");
            Prime.getMySQL().createPortal(guildTwo, guildOne, channelTwo);

            channelOne.getManager().setTopic("Connected to: " + guildTwo.getName()).queue();
            channelTwo.getManager().setTopic("Connected to: " + guildOne.getName()).queue();

            //Send Connected Message
            sendConnectedMessage(channelOne, channelTwo);
        } catch (Exception ignored) {
            Logger.error(ignored);
        }
    }

    private void sendConnectedMessage(TextChannel channelOne, TextChannel channelTwo) {
        //GuildOne Message
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Connection established with " + channelTwo.getGuild().getName(), null, channelTwo.getGuild().getIconUrl());
        embedBuilder.setDescription(":white_check_mark: Successfully created and connected portals.");
        Message message1 = channelOne.sendMessage(embedBuilder.build()).complete();
        channelOne.pinMessageById(message1.getId()).queue();

        //GuildTwo Message
        embedBuilder.setAuthor("Connection established with " + channelOne.getGuild().getName(), null, channelOne.getGuild().getIconUrl());
        embedBuilder.setDescription(":white_check_mark: Successfully created and connected portals.");
        Message message2 = channelTwo.sendMessage(embedBuilder.build()).complete();
        channelTwo.pinMessageById(message2.getId()).queue();
    }

    private void setGuildWaiting(Guild g, TextChannel messageChannel) {
        Prime.getMySQL().updateGuildValue(g, "portal", "waiting");
        if (g.getTextChannelsByName(portalChannelName, true).size() == 0)
            messageChannel.sendMessage(EmbedUtil.success("Portal opened", "Successfully opened portal.\nA portal-channel will be created as soon as another server opens a portal.").build()).queue();
        else
            messageChannel.sendMessage(EmbedUtil.success("Portal opened", "Successfully opened portal.\nA message will be sent to the portal channel as soon as a partner is found.").build()).queue();
        return;
    }

    private void closePortal(CommandManager.ParsedCommandInvocation parsedCommandInvocation) {
        JDA jda = parsedCommandInvocation.getMessage().getJDA();
        Guild messageGuild = parsedCommandInvocation.getMessage().getGuild();
        TextChannel messageChannel = parsedCommandInvocation.getMessage().getTextChannel();

        //Check if portal exists
        String oldGuildPortalEntry = Prime.getMySQL().getGuildValue(messageGuild, "portal");
        if (oldGuildPortalEntry.equals("closed")) {
            messageChannel.sendMessage(EmbedUtil.error("Portal error!", "Portal is already closed").build()).queue();
            return;
        }
        if (oldGuildPortalEntry.equals("waiting")) {
            Prime.getMySQL().updateGuildValue(messageGuild, "portal", "closed");
            messageChannel.sendMessage(EmbedUtil.success("Portal", "Successful closed portal request.").build()).queue();
            return;
        }
        Guild partnerGuild = jda.getGuildById(Prime.getMySQL().getPortalValue(messageGuild, "partnerid"));

        //Close Channels
        TextChannel channelOne = null;
        TextChannel channelTwo = null;
        try {
            channelOne = jda.getTextChannelById(Prime.getMySQL().getPortalValue(messageGuild, "channelid"));
            channelTwo = jda.getTextChannelById(Prime.getMySQL().getPortalValue(partnerGuild, "channelid"));
        } catch (NullPointerException ignored) {
            //Channels doesn't exist
        }

        if (channelOne != null)
            channelOne.getManager().setName(closedChannelName).queue();
        if (channelTwo != null)
            channelTwo.getManager().setName(closedChannelName).queue();

        //Close and delete DB Portal
        Prime.getMySQL().updateGuildValue(messageGuild, "portal", "closed");
        Prime.getMySQL().deletePortal(messageGuild);
        Prime.getMySQL().updateGuildValue(partnerGuild, "portal", "closed");
        Prime.getMySQL().deletePortal(partnerGuild);

        EmbedBuilder portalClosedMessage = new EmbedBuilder();
        portalClosedMessage.setAuthor("Portal closed!", null, jda.getSelfUser().getEffectiveAvatarUrl());
        portalClosedMessage.setDescription("Portal was closed. Create a new one with `" + parsedCommandInvocation.getPrefix() + "portal create`");
        portalClosedMessage.setColor(Colors.COLOR_ERROR);

        channelOne.sendMessage(portalClosedMessage.build()).queue();
        portalClosedMessage.setDescription("Portal was closed. Create a new one with `" + parsedCommandInvocation.getPrefix() + "portal create`");
        channelTwo.sendMessage(portalClosedMessage.build()).queue();

        channelOne.getManager().setTopic("Portal closed").queue();
        channelTwo.getManager().setTopic("Portal closed").queue();
    }

    private void createInviteEntryIfNotExists(Configuration configuration, Guild g) {
        if (!configuration.has(g.getId() + ".state")) {
            configuration.set(g.getId() + ".state", "enabled");
        }
    }
}