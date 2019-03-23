package site.romvoid.forgebot.core;

import java.io.File;
import java.io.FileNotFoundException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import site.romvoid.forgebot.commands.Text;
import site.romvoid.forgebot.commands.commandBug;
import site.romvoid.forgebot.commands.commandHelp;
import site.romvoid.forgebot.commands.commandMemberId;
import site.romvoid.forgebot.commands.commandRaidReport;
import site.romvoid.forgebot.commands.commandRaidStats;
import site.romvoid.forgebot.commands.commandTest;
import site.romvoid.forgebot.commands.commandUserClan;
import site.romvoid.forgebot.commands.admin.commandChkRoles;
import site.romvoid.forgebot.commands.admin.commandPing;
import site.romvoid.forgebot.commands.admin.commandServers;
import site.romvoid.forgebot.commands.admin.commandSettings;
import site.romvoid.forgebot.commands.admin.commandVersion;
import site.romvoid.forgebot.listeners.GuildJoinListener;
import site.romvoid.forgebot.listeners.GuildMemberEvent;
import site.romvoid.forgebot.listeners.MentionListener;
import site.romvoid.forgebot.listeners.ReactionListener;
import site.romvoid.forgebot.listeners.commandListener;
import site.romvoid.forgebot.listeners.readyListener;
import site.romvoid.forgebot.util.MySQL;
import site.romvoid.forgebot.util.SECRETS;
import site.romvoid.forgebot.util.STATIC;


public class Main {

    public static JDABuilder builder;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException{
        System.getProperty("file.encoding");
        System.out.println("[ForgeBot] Starting bot...");
        if(!new File("secrets.json").exists())
            Configuration.create_config();
            MySQL.connect();
            builder = new JDABuilder();
            builder.setToken(SECRETS.token);
            builder.setAutoReconnect(true);
            builder.setGame(STATIC.GAME);
            builder.setStatus(OnlineStatus.ONLINE);

            initializeCommands();
            initializeListeners();

        try {
            builder.build();
        } catch (LoginException e) {
            System.out.println("INVALID KEY!!" );
            String token = Configuration.prompt("token");
            Configuration.set("token", token);
        }
        System.out.println("[ForgeBot] Systems Operational");
    }
    private static void initializeCommands() {

       commandHandler.registerCommand("help", new commandHelp());
       commandHandler.registerCommand("bug", new commandBug());
       commandHandler.registerCommand("rstats", new commandRaidStats());
       commandHandler.registerCommand("clan", new commandUserClan());
       commandHandler.registerCommand("report", new commandRaidReport());
       commandHandler.registerCommand("mem", new commandMemberId());
       commandHandler.registerCommand("test", new commandTest());
       commandHandler.registerCommand("ping", new commandPing());
       commandHandler.registerCommand("version", new commandVersion());
       commandHandler.registerCommand("servers", new commandServers());
       commandHandler.registerCommand("settings", new commandSettings());
       commandHandler.registerCommand("hello", new commandChkRoles());
       commandHandler.registerCommand("text", new Text());

    }
    
    private static void initializeListeners() {
        builder.addEventListener(new commandListener());
        builder.addEventListener(new readyListener());
        builder.addEventListener(new MentionListener());
        builder.addEventListener(new GuildJoinListener());
        builder.addEventListener(new GuildMemberEvent());
        builder.addEventListener(new ReactionListener());
    }

}
