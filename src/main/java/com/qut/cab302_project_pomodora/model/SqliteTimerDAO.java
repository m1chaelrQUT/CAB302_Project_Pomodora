package com.qut.cab302_project_pomodora.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SqliteTimerDAO  implements ITimerDAO {
    private Connection connection;

    public SqliteTimerDAO() {
        connection = SqliteConnection.getInstance();

        // If table doesn't exist
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS timers ("
                    + "timersId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userId INTEGER FOREIGN KEY NOT NULL,"
                    + "workDuration INTEGER NOT NULL,"
                    + "shortBreakDuration INTEGER NOT NULL,"
                    + "longBreakDuration INTEGER NOT NULL,"
                    + "longBreakAfter INTEGER NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: Replace with a more robust logging system
        }
    }

    /**
     * Initializes the user timers with default values.
     * @param user The user to initialize timers for.
     */
    @Override
    public void initializeUserTimers(User user) {
        final int DEFAULT_WORK_DURATION = 25;
        final int DEFAULT_SHORT_BREAK_DURATION = 5;
        final int DEFAULT_LONG_BREAK_DURATION = 15;
        final int DEFAULT_LONG_BREAK_AFTER = 4;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO timers " +
                    "(userId, workDuration, shortBreakDuration, longBreakDuration, longBreakAfter) " +
                    "VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, user.getId());
            statement.setInt(2, DEFAULT_WORK_DURATION);
            statement.setInt(3, DEFAULT_SHORT_BREAK_DURATION);
            statement.setInt(4, DEFAULT_LONG_BREAK_DURATION);
            statement.setInt(5, DEFAULT_LONG_BREAK_AFTER);

            // Execute Insert Query
            int rowsAffected = statement.executeUpdate();

            // If the insert was successful, assign the AI id to the user
            if (rowsAffected > 0) {
                System.out.println("User timers initialized successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * updates the user timers with the given values.
     * @param user The user to update timers for.
     * @param workDuration the work duration
     * @param shortBreakDuration the short break duration
     * @param longBreakDuration the long break duration
     * @param longBreakAfter the amount of work sessions before a long break
     */
    @Override
    public void updateUserTimers(User user, int workDuration, int shortBreakDuration, int longBreakDuration, int longBreakAfter) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE timers SET " +
                    "workDuration = ?, shortBreakDuration = ?, longBreakDuration = ?, longBreakAfter = ? " +
                    "WHERE userId = ?");
            statement.setInt(1, workDuration);
            statement.setInt(2, shortBreakDuration);
            statement.setInt(3, longBreakDuration);
            statement.setInt(4, longBreakAfter);
            statement.setInt(5, user.getId());

            // Execute Update Query
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User timers updated successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
