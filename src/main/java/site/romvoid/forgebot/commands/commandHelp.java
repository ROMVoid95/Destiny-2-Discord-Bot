package site.romvoid.forgebot.commands;

import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.STATIC;
import site.romvoid.forgebot.util.commandLogger;

public class commandHelp implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, final MessageReceivedEvent event) {

        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();
        Message message = event.getMessage();
        String prefix = STATIC.prefix;
        PrivateChannel privch = author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        privch.sendTyping().queue();
        message.delete().queue();

        

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor("Bot-Help", "https://romvoid.site/forgebot",
                        "https://cdn.discordapp.com/avatars/515441251953410048/bf6f786d93775417c8a931ca8e7921f5.png")
                .addField("__**NOTICE**__",
                        "Some commands need Arguments `<>`are important arguments and `[]`are optional \nIf you found any bugs, feel free to contact ROMVoid (https://romvoid.site) or just use the ```"
                                + prefix + "bug <report>``` command",
                        true)
                .addField("Tools", "`" + prefix + "roles` - (Shows you the Server Roles and IDs)\n`"
                        + prefix + "userid <@User>` - (Gets the user's id) \n`" + prefix
                        + "serverinfo` -  (Provied some information about the server\n`" + prefix
                        + "userinfo [@User]` -(Provides some user infomation)\n `" + prefix
                        + "addrole <@User> <role>`(Assigns a role to a user\n `" + prefix
                        + "removerole <@User> <role> (Removes a role from a member)", false)
                .addField("Moderation", "`" + prefix + "clear <count>` - (clears messages)\n`",
                        false)
                .addField("Botinfo", "`" + prefix
                        + "ping` - (Sends the actual ping of the Bot to Discord) \n`" + prefix
                        + "version` - (Sends Bot's version info) \n`" + prefix
                        + "servers` - (Specifies a list of all servers where the bot is running) ",
                        false)
                .addField("Server settings", "`" + prefix
                        + "settings log  toggle`  -  Toggles command logger\n`" + prefix
                        + "settings log channel  <#Channel>`  -  Sets log channel\n`" + prefix
                        + "settings joinmessages toggle`  -  Toggles joinmesasges\n`" + prefix
                        + "settings joinmessages toggle`  -  Toggles joinmesasges\n`" + prefix
                        + "settings joinmessages leave `<Godbye %user%> -  Sets leave message\n`"
                        + prefix
                        + "settings joinmessages channel <#Channel>` - Sets message channel\n`"
                        + prefix + "settings prefix <prefix/reset>`  -  Sets the server's prefix",
                        false);
        privch.sendMessage(embed.build()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("help", event);

    }

    @Override
    public String help() {
        return null;
    }
}
