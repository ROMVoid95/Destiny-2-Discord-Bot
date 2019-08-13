package com.prime.sql;

import com.prime.Prime;
import com.prime.util.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MinecraftSQL implements DatabaseGenerator {

    private Connection connection;
    private MySQL mySQL;

    public MinecraftSQL() {
        this.mySQL = Prime.getMySQL();
        this.connection = MySQL.getCon();
    }

    @Override
    public void createTableIfNotExist() {
        try {
            if (connection.isClosed())
                mySQL.connect();
            PreparedStatement ps = connection.prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS `mincraft` (" +
                    "`id` INT(250) NOT NULL AUTO_INCREMENT," +
                    "`uuid` TEXT," +
                    "`playername` TEXT," +
                    "`awaitingaprooval` TEXT, " +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=3918 DEFAULT CHARSET=utf8;");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }
}
