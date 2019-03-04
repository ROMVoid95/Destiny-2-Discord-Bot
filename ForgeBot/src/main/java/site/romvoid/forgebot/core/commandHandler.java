package site.romvoid.forgebot.core;

import java.util.HashMap;
import site.romvoid.forgebot.commands.Command;

public class commandHandler {

    public static final commandParser parser = new commandParser();
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) throws Exception {

        if (commands.containsKey(cmd.invoke.toLowerCase())) {

            boolean safe = commands.get(cmd.invoke.toLowerCase()).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke.toLowerCase()).action(cmd.args, cmd.event);
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            }

        }

    }
    
    public static void registerCommand(String invoke, Command command) {
        commands.put(invoke, command);
    }
}