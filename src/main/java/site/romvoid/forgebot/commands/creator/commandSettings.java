package site.romvoid.forgebot.commands.creator;

import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;
import site.romvoid.forgebot.core.permissionHandler;
import site.romvoid.forgebot.util.MySQL;
import site.romvoid.forgebot.util.STATIC;
import site.romvoid.forgebot.util.commandLogger;
import site.romvoid.forgebot.util.embedSender;

public class commandSettings implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        message.delete().queue();
        channel.sendTyping().queue();

        if (permissionHandler.checkStaffRole(event)) {
            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }

        if (!(args.length > 0)) {
            embedSender.sendEmbed(help(), channel, Color.red);
            return;
        }
        if(!MySQL.ifGuildExists(guild))
            MySQL.createServer(guild);

        switch (args[0].toLowerCase()) {
            case "info":
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("ForgeBot Settings")
                        .setColor(new Color(0x18BAC0));
                if (!MySQL.getValue(guild, "logchannel").equals("0")) {
                    embed.addField(":vhs: Log channel", "`" + guild.getTextChannelById(MySQL.getValue(guild, "logchannel")).getName() + "`", false);
                } else {
                    embed.addField(":vhs: Log channel", "`disabled`", false);
                }
                embed.addField(":exclamation: Prefix", "`" + MySQL.getValue(guild, "prefix") + "`", false);
                channel.sendMessage(embed.build()).queue();
                break;
            case "log":
                if (!(args.length > 1)) {
                    embedSender.sendEmbed(logHelp(), channel, Color.red);
                    return;
                }

                switch (args[1].toLowerCase()) {
                    case "toggle":
                        if (!MySQL.getValue(guild, "logchannel").equals("0")) {
                            MySQL.updateValue(guild, "logchannel", guild.getDefaultChannel().getId());
                            embedSender.sendEmbed(":white_check_mark: Successfully enabled command logger", channel, Color.green);
                        } else {
                            MySQL.updateValue(guild, "logchannel", "0");
                            embedSender.sendEmbed(":white_check_mark: Successfully disabled command logger", channel, Color.green);

                        }
                        break;
                    case "channel":
                        if (!(message.getMentionedChannels().size() > 0)) {
                            embedSender.sendEmbed("Usage : `" + prefix + "joinmessages channel <#Channel>`", channel, Color.red);
                            return;
                        }
                        MySQL.updateValue(guild, "logchannel", message.getMentionedChannels().get(0).getId());
                        embedSender.sendEmbed(":white_check_mark: Succesfully set logchannel to " + message.getMentionedChannels().get(0).getAsMention(), channel, Color.green);

                }
                break;
            case "prefix":
                if (!(args.length > 1)) {
                    embedSender.sendEmbed(prefixHelp(), channel, Color.red);
                    return;
                }
                if(args[1].equals("reset")){
                    MySQL.updateValue(guild, "prefix", prefix);
                    embedSender.sendEmbed(":repeat: Successfully restored default prefix", channel, Color.green);
                    return;
                }

                if(args[1].length() > 2){
                    embedSender.sendEmbed(":warning: Your prefix can't be longer than 2 chars", channel, Color.red);
                    return;
                }

                MySQL.updateValue(guild, "prefix", args[1]);
                embedSender.sendEmbed(":white_check_mark: Successfully set prefix to `" + args[1] + "` !", channel, Color.green);


        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("settings", event);

    }

    @Override
    public String help() {
        return "USAGE: \n" +
                "`  " + STATIC.prefix + "settings log  toggle`  -  Toggles command logger\n" +
                "`  " + STATIC.prefix + "settings log channel  <#Channel>`  -  Sets log channel\n" +
                "`  " + STATIC.prefix + "settings prefix <prefix/reset>`  -  Sets the server's prefix"
                ;
    }

    public String logHelp() {
        return "USAGE: \n" +
                "`  " + STATIC.prefix + "settings log  toggle`  -  Toggles command logger\n" +
                "`  " + STATIC.prefix + "settings log channel  <#Channel>`  -  Sets log channel\n"
                ;
    }

    public String prefixHelp() {
        return  "`  " + STATIC.prefix + "settings prefix <prefix/reset>`  -  Sets the server's prefix"
                ;
    }
}
