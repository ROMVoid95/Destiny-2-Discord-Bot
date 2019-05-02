package com.prime.commands.moderation;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;
import com.prime.util.StringUtil;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommandClear extends CommandHandler {

    public CommandClear() {
        super(new String[]{"clear", "purge"}, CommandCategory.MODERATION, new PermsNeeded("command.clear", false, false), "Clear the chat.", "<amount of messages> [@User]");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length == 0) {
            return createHelpMessage();
        }

        if (!StringUtil.isNumeric(parsedCommandInvocation.getArgs()[0])) {
            return EmbedUtil.message(EmbedUtil.error("Error!", "Parameter must be numeric."));
        }

        int messageAmount = Integer.parseInt(parsedCommandInvocation.getArgs()[0]);
        User user = (parsedCommandInvocation.getMessage().getMentionedUsers().size() == 1) ? parsedCommandInvocation.getMessage().getMentionedUsers().get(0) : null;

        if (messageAmount < 2) {
            return EmbedUtil.message(EmbedUtil.error("Error!", "I can't delete less than 2 messages."));
        }

        if (messageAmount > 3000) {
            return EmbedUtil.message(EmbedUtil.error("Error!", "Why do you want to clear more than 3000 messages??"));
        }

        int deletedMessagesSize = 0;
        List<Message> messagesToDelete;
        while (messageAmount != 0) {
            if (messageAmount > 100) {
                messagesToDelete = parsedCommandInvocation.getMessage().getTextChannel().getHistory().retrievePast(100).complete();
                messageAmount -= 100;
            } else {
                messagesToDelete = parsedCommandInvocation.getMessage().getTextChannel().getHistory().retrievePast(messageAmount).complete();
                messageAmount = 0;
            }
            messagesToDelete = messagesToDelete.stream().filter(message -> !message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))).collect(Collectors.toList());
            if (user != null)
                messagesToDelete = messagesToDelete.stream().filter(message -> message.getAuthor() == user).collect(Collectors.toList());
            deletedMessagesSize += messagesToDelete.size();
            if (messagesToDelete.size() > 1)
                parsedCommandInvocation.getMessage().getTextChannel().deleteMessages(messagesToDelete).complete();
            else break;
        }
        return EmbedUtil.message(EmbedUtil.success("Cleared channel!", "Successfully cleared `" + deletedMessagesSize + "` messages"));
    }
}
