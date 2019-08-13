package com.prime;

import javax.security.auth.login.LoginException;

import com.prime.command.CommandManager;
import com.prime.commands.botowner.CommandAlarm;
import com.prime.commands.botowner.CommandCreateInvite;
import com.prime.commands.botowner.CommandDBGuild;
import com.prime.commands.botowner.CommandEval;
import com.prime.commands.botowner.CommandGenerateDocsJSON;
import com.prime.commands.botowner.CommandGlobalBlacklist;
import com.prime.commands.botowner.CommandGuildData;
import com.prime.commands.botowner.CommandGuilds;
import com.prime.commands.botowner.CommandLFGRules;
import com.prime.commands.botowner.CommandMaintenance;
import com.prime.commands.botowner.CommandPlay;
import com.prime.commands.botowner.CommandRestart;
import com.prime.commands.botowner.CommandStop;
import com.prime.commands.botowner.CommandTest;
import com.prime.commands.destiny.CommandChangeNickname;
import com.prime.commands.destiny.CommandClanInfo;
import com.prime.commands.destiny.CommandRaidReport;
import com.prime.commands.general.CommandFeedback;
import com.prime.commands.general.CommandGitBug;
import com.prime.commands.general.CommandHelp;
import com.prime.commands.general.CommandInfo;
import com.prime.commands.general.CommandInvite;
import com.prime.commands.general.CommandServerInfo;
import com.prime.commands.general.CommandUptime;
import com.prime.commands.moderation.CommandAutochannel;
import com.prime.commands.moderation.CommandClear;
import com.prime.commands.moderation.CommandMakeEmbed;
import com.prime.commands.moderation.CommandMoveAll;
import com.prime.commands.moderation.CommandPortal;
import com.prime.commands.moderation.CommandRanks;
import com.prime.commands.moderation.CommandRole;
import com.prime.commands.moderation.CommandRoleReactionMessage;
import com.prime.commands.moderation.CommandSearch;
import com.prime.commands.moderation.CommandUserInfo;
import com.prime.commands.moderation.CommandVerification;
import com.prime.commands.settings.CommandAutorole;
import com.prime.commands.settings.CommandJoinMessage;
import com.prime.commands.settings.CommandLeaveMessage;
import com.prime.commands.settings.CommandLog;
import com.prime.commands.settings.CommandPrefix;
import com.prime.commands.settings.CommandWelcomeChannel;
import com.prime.core.ListenerManager;
import com.prime.features.GiveawayHandler;
import com.prime.features.RemindHandler;
import com.prime.features.VerificationKickHandler;
import com.prime.features.VerificationUserHandler;
import com.prime.permission.PermissionManager;
import com.prime.sql.DatabaseManager;
import com.prime.sql.MemberSQL;
import com.prime.sql.MinecraftSQL;
import com.prime.sql.MySQL;
import com.prime.sql.ServerLogSQL;
import com.prime.sql.VerificationKickSQL;
import com.prime.sql.VerificationUserSQL;
import com.prime.sql.WarnSQL;
import com.prime.util.Configuration;
import com.prime.util.Info;
import com.prime.util.Logger;
import com.prime.util.Setup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 * PrimeBot main class. Initializes all components.
 *
 * @author ROMVoid
 */
public class Prime {

  private static final SimpleDateFormat timeStampFormatter =
      new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
  private static final String dataFolder = "data/";
  private static Prime instance;
  private final MySQL mySQL;
  private final Configuration configuration;
  private final CommandManager commandManager;
  private JDA jda;
  private final Timer timer;
  private final Set<EventListener> eventListeners;
  private final PermissionManager permissionManager;
  private final DatabaseManager databaseManager;
  private static final String[] CONFIG_KEYS =
      {"token", "prefix", "name", "version", "github", "mysql_host", "mysql_port", "mysql_database",
          "mysql_password", "mysql_user", "cmdLogChannel", "support_server", "support_staff",
          "premium_role", "dev_channel_id", "git_token", "bungie_api_key", "maintenance"};


  /**
   * Constructs PrimeBot.
   */
  private Prime() {
    instance = this;
    // initialize logger
    new File("logs").mkdirs();
    Logger.logInFile("D2Bot", "-current", "/logs/");

    timer = new Timer();
    eventListeners = new HashSet<>();
    databaseManager = new DatabaseManager();

    // load configuration and obtain missing config values
    new File(dataFolder).mkdirs();

    configuration = new Configuration(new File(Info.CONFIG_FILE));
    for (String configKey : CONFIG_KEYS) {
      if (!configuration.has(configKey)) {
        String input = Setup.prompt(configKey);
        configuration.set(configKey, input);
      }
    }

    // load MySQL adapter
    mySQL = new MySQL(Info.MYSQL_HOST, Info.MYSQL_PORT, Info.MYSQL_USER, Info.MYSQL_PASSWORD,
        Info.MYSQL_DATABASE);
    mySQL.connect();

    // Create databases if neccesary
    generateDatabases();


    commandManager = new CommandManager();
    registerCommandHandlers();
    permissionManager = new PermissionManager();
    // init JDA
    initJDA();

    // init features
    new GiveawayHandler();
    new RemindHandler();
    VerificationUserHandler.loadVerifyUser();
    VerificationKickHandler.loadVerifyKicks();

    String maintenanceStatus = getConfiguration().getString("maintenance");
    if (maintenanceStatus.equalsIgnoreCase("1")) {
      CommandMaintenance.enable();
    }
  }

  /**
   * Initializes the bot.
   *
   * @param args command line parameters.
   */
  public static void main(String[] args) {
    if (instance != null)
      throw new RuntimeException("PrimeBot has already been initialized in this VM.");
    new Prime();
  }

  /**
   * Initializes the JDA instance.
   */
  public static void initJDA() {
    if (instance == null)
      throw new NullPointerException("PrimeBot has not been initialized yet.");

    JDABuilder builder = new JDABuilder(AccountType.BOT);
    builder.setToken(instance.configuration.getString("token"));
    builder.setStatus(OnlineStatus.DO_NOT_DISTURB);

    // add all EventListeners
    for (EventListener listener : instance.eventListeners)
      builder.addEventListener(listener);

    new ListenerManager(builder);

    try {
      instance.jda = builder.build();
    } catch (LoginException e) {
      Logger.error(e.getMessage());
    }

    getJDA().getPresence().setStatus(OnlineStatus.ONLINE);

    Info.lastRestart = new Date();
  }

  /**
   * Registers all command handlers used in this project.
   *
   * @see CommandManager
   */
  private void registerCommandHandlers() {
    // Usage: commandManager.registerCommandHandler(yourCommandHandler...);

    // moderation commands package
    commandManager.registerCommandHandlers(new CommandPortal(), new CommandVerification(),
        new CommandAutochannel(), new CommandSearch(), new CommandServerInfo(),
        new CommandUserInfo(), new CommandRanks(), new CommandClear(), new CommandRoleReactionMessage(),
        new CommandMoveAll(), new CommandRole(), new CommandMakeEmbed());

    // botowner commands package
    commandManager.registerCommandHandlers(new CommandDBGuild(), new CommandPlay(),
        new CommandRestart(), new CommandStop(), new CommandGuilds(), new CommandCreateInvite(),
        new CommandEval(), new CommandGlobalBlacklist(), new CommandGenerateDocsJSON(),
        new CommandMaintenance(), new CommandGuildData(), new CommandAlarm(), new CommandLFGRules(), new CommandTest());

    // general commands package
    commandManager.registerCommandHandlers(new CommandHelp(), new CommandFeedback(),
        new CommandInfo(), new CommandInvite(), new CommandUptime(), new CommandGitBug());

    // destiny commands package
    commandManager.registerCommandHandlers(new CommandChangeNickname(), new CommandClanInfo(),
        new CommandRaidReport());

    // settings commands package
    commandManager.registerCommandHandlers(new CommandAutorole(), new CommandJoinMessage(),
        new CommandPrefix(), new CommandWelcomeChannel(), new CommandLeaveMessage(),
        new CommandLog());

    // also register commands from the old framework
    new CommandManager();
  }

  private void generateDatabases() {
    databaseManager.addGenerators(new ServerLogSQL(), new WarnSQL(), new MemberSQL(),
        new VerificationKickSQL(), new VerificationUserSQL(), new MinecraftSQL());

    databaseManager.generate();

  }

  /**
   * @return the MySQL adapter.
   */
  public static MySQL getMySQL() {
    return instance == null ? null : instance.mySQL;
  }

  /**
   * @return the bot configuration.
   */
  public static Configuration getConfiguration() {
    return instance == null ? null : instance.configuration;
  }

  /**
   * @return the JDA instance.
   */
  public static JDA getJDA() {
    return instance == null ? null : instance.jda;
  }

  /**
   * @return the CommandManager.
   */
  public static CommandManager getCommandManager() {
    return instance == null ? null : instance.commandManager;
  }

  /**
   * @return the {@link PermissionManager}.
   */
  public PermissionManager getPermissionManager() {
    return permissionManager;
  }

  /**
   * @return the {@link PermissionManager} via a static reference.
   */
  public static PermissionManager sGetPermissionManager() {
    return instance == null ? null : instance.permissionManager;
  }

  /**
   * @return a timer.
   */
  public static Timer getTimer() {
    return instance == null ? null : instance.timer;
  }

  /**
   * Adds an EventListener to the event pipe. EventListeners registered here will be re-registered
   * when the JDA instance is initialized again.
   *
   * @param listener the EventListener to register.
   * @return false if the bot has never been initialized or if the EventListener is already
   *         registered.
   */
  public static boolean registerEventListener(EventListener listener) {
    if (instance != null && instance.eventListeners.add(listener)) {
      if (instance.jda != null)
        instance.jda.addEventListener(listener);
      return true;
    }
    return false;
  }

  /**
   * @return a freshly generated timestamp in the 'dd.MM.yyyy HH:mm:ss' format.
   */
  public static String getNewTimestamp() {
    return timeStampFormatter.format(new Date());
  }

  /**
   * @return the data folder path
   */
  public static String getDataFolder() {
    return dataFolder;
  }

}
