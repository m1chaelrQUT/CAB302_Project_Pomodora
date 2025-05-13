package com.qut.cab302_project_pomodora.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;

    private SqliteConnection() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Connection getInstance() {
        if (instance == null) {
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
