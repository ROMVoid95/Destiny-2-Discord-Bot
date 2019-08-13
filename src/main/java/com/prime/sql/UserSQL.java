package com.prime.sql;

import com.prime.Prime;
import com.prime.util.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

/**
 * @author ROMVoid
 */
public class UserSQL implements DatabaseGenerator {

	private static Connection connection;
	private MySQL mySQL;
	private User user;

	/**
	 * Uses for database generation
	 */
	public UserSQL() {
		this.mySQL = Prime.getMySQL();
		UserSQL.connection = MySQL.getCon();
	}

	public UserSQL(User user) {
		this.user = user;
		this.mySQL = Prime.getMySQL();
		UserSQL.connection = MySQL.getCon();

		create();
	}

	private UserSQL(User user, MySQL mySQL, Connection connection) {
		this.user = user;
		this.mySQL = mySQL;
		UserSQL.connection = connection;
	}

	public static UserSQL fromUser(User user) {
		return new UserSQL(user, Prime.getMySQL(), MySQL.getCon());
	}

	public static UserSQL fromMember(Member member) {
		return fromUser(member.getUser());
	}

	// User Stuff
	public boolean exist() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE discordid = ?");
			ps.setString(1, user.getId());
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
			PreparedStatement ps = connection
					.prepareStatement("UPDATE users SET " + type + " = '" + value + "' WHERE discordid = ?");
			ps.setString(1, user.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String get(String type) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE `discordid` = ?");
			ps.setString(1, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean existBungie() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE discordId = ?");
			ps.setString(1, user.getId());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getData(String discordId) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE `discordid` = ?");
			ps.setString(1, discordId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String d_id = rs.getString(1);
				String bnet = rs.getString(2);
				String b_id = rs.getString(3);
				String all = String.format("%s:%s:%s", d_id, bnet, b_id);
				return all;
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
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO users(`discordid`, `bnet`, `bungieid') VALUES (?, ?, ?)");
			ps.setString(1, user.getId());
			ps.setString(2, "No bio set.");
			ps.setString(3, "1000");
			ps.setString(4, "false");
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isPremium() {
		String entry = get("premium");
		if (entry.equalsIgnoreCase("false")) {
			return false;
		}
		Date expiry = new Date(Long.parseLong(this.get("premium")));
		Date now = new Date();
		if (expiry.before(now)) {
			this.set("premium", "false");
			return false;
		}
		return true;
	}

	public Date getPremiumExpiryDate() {
		if (!this.isPremium())
			return null;
		return new Date(Long.parseLong(this.get("premium")));
	}

	public String formatExpiryDate() {
		if (!this.isPremium())
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(this.getPremiumExpiryDate());
	}

	public User getUser() {
		return Prime.getJDA().getUserById(this.get("userid"));
	}

	public Member getMember(Guild guild) {
		return guild.getMember(this.getUser());
	}

	public MemberSQL getMemberSQL(Guild guild) {
		return MemberSQL.fromUser(this.user, guild);
	}

	@Override
	public void createTableIfNotExist() {
		try {
			if (connection.isClosed())
				mySQL.connect();
			PreparedStatement ps = connection.prepareStatement("" + "CREATE TABLE IF NOT EXISTS `users` ("
					+ "  `discordid` INT(30) NOT NULL AUTO_INCREMENT," + "  `bnet` TEXT NOT NULL,"
					+ "  `bungieid` TEXT NOT NULL,"
					+ "  PRIMARY KEY (`discordid`)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			ps.execute();
		} catch (SQLException e) {
			Logger.error(e);
		}
	}

}
