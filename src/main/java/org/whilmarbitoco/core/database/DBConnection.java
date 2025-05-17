package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection conn;

    public static Connection getConnection() {
        String url = Config.dbUrl();
        String user = Config.dbUser();
        String password = Config.dbPassword();

        try {
            if (conn == null) {
                conn = DriverManager.getConnection(url, user, password);;
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

}
