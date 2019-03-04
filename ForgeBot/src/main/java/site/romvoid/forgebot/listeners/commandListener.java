package site.romvoid.forgebot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import site.romvoid.forgebot.core.commandHandler;
import site.romvoid.forgebot.util.MySQL;


public class commandListener extends ListenerAdapter {



    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!MySQL.ifGuildExists(event.getGuild())){
            MySQL.createServer(event.getGuild());
        }
        String prefix = MySQL.getValue(event.getGuild(), "prefix");
        if (event.getMessage().getContentRaw().startsWith(prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            try {
                commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}