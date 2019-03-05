package site.romvoid.forgebot.db;

import java.sql.SQLException;
import java.util.HashMap;
import site.romvoid.forgebot.core.BotConfig;

public class WebDb {

    private static final String DEFAULT_CONNECTION = "discord";
    private static HashMap<String, MySQLAdapter> connections = new HashMap<>();

    public static MySQLAdapter get(String key) {
        if (connections.containsKey(key)) {
            return connections.get(key);
        }
        System.out.println(String.format("The MySQL connection '%s' is not set!", key));
        return null;
    }

    public static MySQLAdapter get() {
        return connections.get(DEFAULT_CONNECTION);
    }

    public static void init() {
        connections.clear();
        connections.put("discord", new MySQLAdapter(BotConfig.DB_HOST, BotConfig.DB_PORT, BotConfig.DB_USER, BotConfig.DB_PASS, BotConfig.DB_NAME));
        try {
            get().query("SET NAMES utf8mb4");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("COULD NOT SET utf8mb4");
        }
    }
}