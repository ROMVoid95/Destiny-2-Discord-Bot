package com.prime.commands.botowner;

import net.dv8tion.jda.core.entities.Message;
import com.prime.Prime;
import com.prime.command.CommandCategory;
import com.prime.command.CommandHandler;
import com.prime.command.CommandManager;
import com.prime.permission.PermsNeeded;
import com.prime.permission.UserPermissions;
import com.prime.util.Info;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandGenerateDocsJSON extends CommandHandler {

    public CommandGenerateDocsJSON() {
        super(new String[]{"generate-docs"}, CommandCategory.BOT_OWNER, new PermsNeeded("command.generate-docs", false, false), "Generate json file fou our docs.", "");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation parsedCommandInvocation, UserPermissions userPermissions) {
        //Generate JSON File for website
        int i = 0;
        StringBuilder out = new StringBuilder();
        List<CommandHandler> allCommands = new ArrayList<>();
        for (CommandHandler commandHandler : Prime.getCommandManager().getCommandAssociations().values()) {
            if (!allCommands.contains(commandHandler))
                allCommands.add(commandHandler);
        }
        for (CommandHandler commandHandler : allCommands) {
            if (commandHandler.getCategory().equals(CommandCategory.BOT_OWNER))
                continue;
            StringBuilder usage = new StringBuilder();
            for (String part : commandHandler.getParameterUsage().split("\n")) {
                if (commandHandler.getParameterUsage().split("\n").length > 1) {
                    usage.append(Info.BOT_DEFAULT_PREFIX + commandHandler.getCommandAliases()[0] + " " + part + "<br>");
                } else
                    usage.append(Info.BOT_DEFAULT_PREFIX + commandHandler.getCommandAliases()[0] + " " + part + "");
            }
            out.append("{\n\"id\":\"" + i + "\",\"name\":\"" + commandHandler.getCommandAliases()[0] + "\",\n" +
                    "\t\"command\":\"" + Info.BOT_DEFAULT_PREFIX + commandHandler.getCommandAliases()[0] + "\",\n" +
                    "\t\"description\":\"" + commandHandler.getDescription() + "\",\n" +
                    "\t\"category\":\"" + commandHandler.getCategory().getId() + "\",\n" +
                    "\t\"aliases\":\"" + String.join(",", commandHandler.getCommandAliases()) + "\",\n" +
                    "\t\"permissions\":\"" + commandHandler.getPermsNeeded().getRequiredPermissionNode() + "\",\n" +
                    "\t\"usage\":\"" + usage + "\"\n},\n");
            i++;
        }
        try {
            File file = createCommandListFile();
            FileWriter writer = new FileWriter(file);
            String json = out.toString();
            String cutted = json.substring(0, json.length() - 2);
            cutted = "[" + cutted + "]";
            writer.write(cutted);
            writer.flush();
            writer.close();
            parsedCommandInvocation.getTextChannel().sendFile(file).complete();
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createCommandListFile() {
        new File("data/temp").mkdirs();
        File file = new File("command-list.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
