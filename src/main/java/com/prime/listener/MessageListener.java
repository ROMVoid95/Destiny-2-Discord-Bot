package com.prime.listener;

import com.prime.commands.general.CommandFeedback;
import com.prime.commands.general.CommandGitBug;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        CommandGitBug.handle(event);
        CommandFeedback.handle(event);
    }

}
