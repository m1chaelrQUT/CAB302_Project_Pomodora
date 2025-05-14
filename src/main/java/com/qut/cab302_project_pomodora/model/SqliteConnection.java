package com.qut.cab302_project_pomodora.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SqliteConnection is a singleton class that manages the connection to the SQLite database.
 * It provides a method to get the instance of the connection and allows for testing with a mock connection.
 */
public class SqliteConnection {
    private static Connection instance = null;
    private static boolean isTest = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private SqliteConnection() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Sets the instance to a test instance.
     */
    public static void setTestInstance(Connection testConnection) {
        instance = testConnection;
        isTest = true;
    }

    /**
     * Resets the instance to null and sets isTest to false.
     */
    public static void reset() {
        instance = null;
        isTest = false;
    }

    /**
     * Sets the instance to a test instance.
     */
    public static Connection getInstance() {
        if (instance == null && !isTest) {
            String url = "jdbc:sqlite:pomodora.db";
            try {
                instance = DriverManager.getConnection(url);
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace(System.err);
            }
        }
        return instance;
    }
}
