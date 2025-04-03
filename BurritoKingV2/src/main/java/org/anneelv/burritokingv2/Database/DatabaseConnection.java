package org.anneelv.burritokingv2.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
 * The class that manages the database connection for the application.
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:burritoKing.db";

    /*
    * Establishes and returns a connection to the database.
    * */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
