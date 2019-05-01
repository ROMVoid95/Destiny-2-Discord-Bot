package site.romvoid.forgebot.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.commands.Command;
import site.romvoid.forgebot.util.STATIC;
import site.romvoid.forgebot.util.commandLogger;

public class commandVersion implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, final MessageReceivedEvent event) {
    	
    	MessageChannel channel = event.getChannel();
    	String img = "https://cdn.discordapp.com/avatars/517867085628702721/14c49784eb9cb447ffa067b5e3e7afb5.png";
    	
    	EmbedBuilder embed = new EmbedBuilder()
    	        .addField("Bot Name", STATIC.BOTNAME, true)
    	        .addField("Bot Version", STATIC.VERSION, true)
    	        .addField("Github Link", "[Link](" + STATIC.GITHUB_LINK + ")", false)
    	        .addField("Patreon Link", "[ROMVoid Patreon](https://www.patreon.com/romvoid)", false)
    	        .addField("Devs", STATIC.OWNER, false)
    	        .setThumbnail(img);

    	channel.sendMessage(embed.build()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("version", event);

    }

    @Override
    public String help() {
        return null;
    }
}
