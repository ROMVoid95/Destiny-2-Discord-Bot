package com.prime.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.EmbedUtil;
import com.prime.util.Info;
import com.prime.util.SafeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles the 'feedback' command which sends a feedback message to the developer server.
 */
public class CommandFeedback extends CommandHandler {
    private static HashMap<Long, ReportHolder> reportMap = new HashMap<>();

    public CommandFeedback() {
        super(new String[]{"feedback", "submitidea"}, CommandCategory.GENERAL,
                new PermsNeeded("command.feedback", false, true),
                "Sends a feedback message to the developers.", "<Feedback title>");
    }

    private static final String ISSUE_HEADER = "<p><strong>Feedback</strong><br><br><strong>Feedback by ";
    private static final String ISSUE_SUFFIX = " </strong><br><br><strong>Description</strong><br><br></p>";


    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        if (parsedCommandInvocation.getArgs().length<1){
            return createHelpMessage();
        }
        Message infoMessage = SafeMessage.sendMessageBlocking(parsedCommandInvocation.getTextChannel(), EmbedUtil.message(new EmbedBuilder().setTitle("Description....").setDescription("Please enter a short description.").setFooter("Will abort in 60 seconds.", null)));
        reportMap.put(parsedCommandInvocation.getAuthor().getIdLong(), new ReportHolder(
                parsedCommandInvocation.getTextChannel(),
                parsedCommandInvocation.getMessage().getContentDisplay().replace(parsedCommandInvocation.getPrefix() + parsedCommandInvocation.getCommandInvocation() + " ", ""),
                parsedCommandInvocation.getAuthor(),
                infoMessage
        ));
        return null;
    }

    public static void handle(MessageReceivedEvent event) {
        if (!reportMap.containsKey(event.getAuthor().getIdLong())) {
            return;
        }
        ReportHolder reportHolder = reportMap.get(event.getAuthor().getIdLong());
        if (event.getMessage().getContentDisplay().contains(reportHolder.title))
            return;
        if (!event.getTextChannel().getId().equals(reportHolder.textChannel.getId()))
            return;
        String description = event.getMessage().getContentDisplay();
        try {
            GitHub gitHub = GitHub.connectUsingOAuth(Info.GITHUB_TOKEN);
            GHRepository repository = gitHub.getOrganization("ROM-Development").getRepository("Destiny-2-Discord-Bot");
            GHIssue issue = repository.createIssue(reportHolder.title).body(ISSUE_HEADER + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + ISSUE_SUFFIX + description).label("Enhancement").label("Up for grabs").create();
            reportHolder.delete(issue.getHtmlUrl().toString());
            event.getMessage().delete().queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ReportHolder {
        private TextChannel textChannel;
        private String title;
        private User author;
        private Message infoMessage;

        private Timer timer;

        private ReportHolder(TextChannel textChannel, String title, User author, Message infoMessage) {
            this.textChannel = textChannel;
            this.title = title;
            this.author = author;
            this.infoMessage = infoMessage;

            //Abort
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    infoMessage.delete().queue();
                    SafeMessage.sendMessage(textChannel, EmbedUtil.message(EmbedUtil.error("Aborted!", "Aborted feedback report.")));
                    reportMap.remove(author.getIdLong());
                }
            }, 60000);
        }

        private void delete(String link) {
            reportMap.remove(author.getIdLong());
            timer.cancel();
            SafeMessage.sendMessage(textChannel, EmbedUtil.message(EmbedUtil.success("Success!", "Successfully sent feedback. [Your Feedback](" + link + ")")));
            infoMessage.delete().queue();
        }
    }
}

