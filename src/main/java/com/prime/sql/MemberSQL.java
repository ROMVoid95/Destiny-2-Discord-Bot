package com.prime.sql;

import com.prime.Prime;
import com.prime.util.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

/**
 * @author ROMVoid
 */
public class MemberSQL implements DatabaseGenerator {

    private Connection connection;
    private MySQL mySQL;
    private Member member;
    private User user;

    /**
     * Uses for database generation
     */
    public MemberSQL() {
        mySQL = Prime.getMySQL();
        connection = MySQL.getCon();
    }

    /**
     * User fromUser(User user, Guild guild) or fromMember(Member member) method
     *
     * @see MemberSQL
     */
    public MemberSQL(Member member) {
        this.member = member;
        this.user = member.getUser();
        this.mySQL = Prime.getMySQL();
        this.connection = MySQL.getCon();

        create();
    }

    private MemberSQL(Member member, MySQL mySQL) {
        this.member = member;
        this.user = member.getUser();
        this.mySQL = Prime.getMySQL();
        this.connection = MySQL.getCon();
    }

    public static MemberSQL fromMember(Member member) {
        return new MemberSQL(member, Prime.getMySQL());
    }

    public static MemberSQL fromUser(User user, Guild guild) {
        return new MemberSQL(guild.getMember(user), Prime.getMySQL());
    }


    //User Stuff
    public boolean exist() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM members WHERE userid = ? AND serverid = ?");
            ps.setString(1, user.getId());
            ps.setString(2, member.getGuild().getId());
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
            PreparedStatement ps = connection.prepareStatement("UPDATE members SET " + type + " = '" + value + "' WHERE userid = ? AND serverid = ?");
            ps.setString(1, user.getId());
            ps.setString(2, member.getGuild().getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String get(String type) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM members WHERE userid = ? AND serverid = ?");
            ps.setString(1, user.getId());
            ps.setString(2, member.getGuild().getId());
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

    public void create() {
        if (exist())
            return;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO members(`id`, `userid`, `serverid`, `permissionlevel`, `level`, `points`) VALUES (0, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, member.getGuild().getId());
            if (member.isOwner())
                ps.setString(3, "3");
            else
                ps.setString(3, "0");
            ps.setString(4, "0");
            ps.setString(5, "0");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserSQL getUserSQL() {
        return UserSQL.fromUser(this.user);
    }

    @Override
    public void createTableIfNotExist() {
        try {
            if (connection.isClosed())
                mySQL.connect();
            PreparedStatement ps = connection.prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS `members` (" +
                    "  `id` INT(250) NOT NULL AUTO_INCREMENT," +
                    "  `userid` VARCHAR(50) NOT NULL," +
                    "  `serverid` VARCHAR(50) NOT NULL," +
                    "  `permissionlevel` VARCHAR(50) NOT NULL," +
                    "  `level` VARCHAR(50) NOT NULL," +
                    "  `points` VARCHAR(50) NOT NULL," +
                    "  PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=3918 DEFAULT CHARSET=utf8;");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }
}
