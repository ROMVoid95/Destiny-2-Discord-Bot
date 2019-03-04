package site.romvoid.forgebot.commands;

import java.awt.Color;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import site.romvoid.forgebot.util.destiny.pvp.BaseStats;

public class commandTest implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
	return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        String name = event.getMember().getEffectiveName();
        String nickname = null;
        if (name.contains("["))
            nickname = name.replaceFirst(
                    Pattern.quote(name.substring(name.indexOf("["), name.indexOf("]") + 1)), "")
                    .replaceAll(" ", "");
        else
            nickname = event.getMember().getEffectiveName();

            String kda = BaseStats.getKd(nickname);
            
            event.getTextChannel()
            .sendMessage(new EmbedBuilder().setColor(Color.green)
                    .setTitle(nickname + " PvP Kills/Death Ratio \n ")
                    .addField("Just TRYY", kda ,true).build())
            .queue();
        }

        
        
    

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
    }

    @Override
    public String help() {
	return null;
    }

}
