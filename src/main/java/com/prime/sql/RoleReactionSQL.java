package com.prime.sql;

import com.prime.Prime;
import com.prime.util.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.dv8tion.jda.core.entities.Guild;

public class RoleReactionSQL implements DatabaseGenerator {
	private static Guild guild;
    private static Connection connection;
    private static MySQL mySQL;

    public RoleReactionSQL() {
        RoleReactionSQL.mySQL = Prime.getMySQL();
        RoleReactionSQL.connection = MySQL.getCon();
    }
    
    public static String getChannel(String type) {
        try {
            if (connection.isClosed())
                mySQL.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT" + type + " FROM rolereaction WHERE `guildid` = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(type);
            }
        } catch (SQLException e) {
            Logger.error(e);
        }
        return null;
    }

    public void set(String type, String value) {
        try {
            if (connection.isClosed())
                mySQL.connect();
            PreparedStatement ps = connection.prepareStatement("UPDATE rolereaction SET " + type + "=? WHERE guildid=?");
            ps.setString(1, value);
            ps.setString(2, guild.getId());
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

	@Override
	public void createTableIfNotExist() {
        try {
            if (connection.isClosed())
                mySQL.connect();
            PreparedStatement ps = connection.prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS `rolereaction` (" +
                    "`channel` TEXT," +
                    "`message` TEXT," +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }
}
