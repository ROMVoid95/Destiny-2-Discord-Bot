package site.romvoid.forgebot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class MySQL {

    private static String password = SECRETS.password;
    public static Connection connection;

    public static void connect(){
        if(!isConnected()){
            String host = STATIC.HOST;
            String port = STATIC.PORT;
            String database = STATIC.DATABASE;
            String username = STATIC.USERNAME;
            try{
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8", username, password);
                System.out.println("[ForgeBot] MySQL connected");
            } catch (SQLException e) {System.out.println("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8");
                System.out.println("[ForgeBot] MySQL connection failed");
                e.printStackTrace();
            }
        }
    }
    
    public static void disconnect(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected(){
        return (connection != null);
    }

    public static boolean ifGuildExists(Guild guild){
        try {
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM guilds WHERE id = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static void createServer(Guild guild){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `guilds`(`id`,`prefix`, `logchannel`) VALUES (?, ?, ?)");
            ps.setString(1, guild.getId());
            ps.setString(2, STATIC.PREFIX);
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateValue(Guild guild, String type, String value){
        try{
            if(connection.isClosed())
                connect();
            if(!ifGuildExists(guild))
                createServer(guild);
            PreparedStatement ps = connection.prepareStatement("UPDATE guilds SET " + type + " = '" + value + "' WHERE id = " + guild.getId());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String getValue(Guild guild, String type){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM guilds WHERE `id` = ?");
            ps.setLong(1, guild.getIdLong());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString(type);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean ifMemberExists(User user){
        try {
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE discordid = ?");
            ps.setString(1, user.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return  false;
    }

    
    public static void createUser(String memberId, String nickname, String id) {
        try {
            if (connection.isClosed()) 
                connect();
                PreparedStatement ps = connection.prepareStatement
                        ("INSERT INTO `users`(`discordid`, `bnetid`, `bungieid`) VALUES (?, ?, ?)");
                ps.setString(1, memberId);
                ps.setString(2, nickname);
                ps.setString(3, id);
                ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void setLeviStats(String nickname, int total, int normal, int prestige, int guide) {
        try {
            if (connection.isClosed()) 
                connect();
                PreparedStatement ps = connection.prepareStatement
                        ("INSERT INTO `levi`(`nickname`, `t`, `n`, `p`, `g`) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, nickname);
                ps.setInt(2, total);
                ps.setInt(3, normal);
                ps.setInt(4, prestige);
                ps.setInt(5, guide);
                ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
