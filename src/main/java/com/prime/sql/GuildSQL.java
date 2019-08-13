package com.prime.sql;

import com.prime.Prime;
import com.prime.util.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.dv8tion.jda.core.entities.Guild;

public class GuildSQL implements DatabaseGenerator {

    private Connection connection;
    private MySQL mySQL;
    private Guild guild;

    @Override
    public void createTableIfNotExist() {
        try {
            if (connection.isClosed())
                mySQL.connect();     
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `guilds`" +
                    "(`serverid` VARCHAR(100) , " +
                    "`prefix` VARCHAR (25)," +
                    "`joinmsg` TEXT," +
                    "`leavemsg` TEXT," +
                    "`channel` TEXT," +
                    "`logchannel` TEXT," +
                    "`autorole` TEXT," +
                    "`portal` VARCHAR (250)," +
                    "`welmsg` TEXT," +
                    "`autochannels` VARCHAR (250)," +
                    "`cases` INT (11)," +
                    "`blacklist` TEXT," +
                    "`whitelist` TEXT);");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private GuildSQL(Guild guild, MySQL mySQL) {
        this.guild = guild;
        this.mySQL = Prime.getMySQL();
        this.connection = MySQL.getCon();
    }

    public static GuildSQL fromGuild(Guild guild) {
        return new GuildSQL(guild, Prime.getMySQL());
    }


    public boolean exist() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM guilds WHERE serverid = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void set(String type, String value) {
        try {
            if (!exist())
                create();
            PreparedStatement ps = connection.prepareStatement("UPDATE guilds SET " + type + " = '" + value + "' WHERE serverid = ?");
            ps.setString(1, guild.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void create() {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `guilds`(`serverid`, `channel`, `prefix`, `joinmsg`, `leavemsg`, `logchannel`, `autorole`, `portal`, `welmsg`, `autochannels`, `blacklist`,`lvlmsg`, `whitelist`) VALUES (?, '0', '>>', 'Welcome %user% on %guild%', 'Bye %user%', '0', '0', 'closed', '0', '', '', '')");
            ps.setString(1, String.valueOf(guild.getIdLong()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String get(String type) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM guilds WHERE serverid = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            // Only returning one result
            if (rs.next()) {
                return rs.getString(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public boolean enabledWhitelist() {
//        return !get("whitelist").equals("");
//    }
//
//    public boolean enabledBlacklist() {
//        return !get("blacklist").equals("");
//    }
//
//    public boolean isBlacklisted(TextChannel channel) {
//        return get("blacklist").contains(channel.getId());
//    }
//
//    public boolean isWhitelisted(TextChannel channel) {
//        return get("whitelist").contains(channel.getId());
//    }

}