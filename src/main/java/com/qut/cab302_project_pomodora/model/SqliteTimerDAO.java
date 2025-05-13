package com.qut.cab302_project_pomodora.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteTimerDAO  implements ITimerDAO {
    private Connection connection;
    // Default timer values
    final int DEFAULT_WORK_DURATION = 25;
    final int DEFAULT_SHORT_BREAK_DURATION = 5;
    final int DEFAULT_LONG_BREAK_DURATION = 15;
    final int DEFAULT_LONG_BREAK_AFTER = 4;

    /**
     * Constructor for SqliteTimerDAO.
     * Initializes the connection to the database and creates the timers table if it doesn't exist.
     */
    public SqliteTimerDAO() {
        connection = SqliteConnection.getInstance();

        // If table doesn't exist
        createTable();
    }

    /**
     * Creates the timers table if it doesn't exist.
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS timers ("
                    + "timersId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userId INTEGER NOT NULL,"
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
     * Updates the user timers with the given values.
     * @param user
     * @param timer
     */
    @Override
    public void updateUserTimers(User user, Timer timer) {

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE timers SET " +
                    "workDuration = ?, shortBreakDuration = ?, longBreakDuration = ?, longBreakAfter = ? " +
                    "WHERE userId = ?");
            // Set the parameters for the prepared statement
            statement.setInt(1, timer.getWorkDuration());
            statement.setInt(2, timer.getShortBreakDuration());
            statement.setInt(3, timer.getLongBreakDuration());
            statement.setInt(4, timer.getLongBreakAfter());
            statement.setInt(5, user.getId());
            // Execute Update Query
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User's timer updated successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the timer for the user.
     * @param user The user to retrieve the timer for.
     * @return The timer for the user.
     */
    @Override
    public Timer getUserTimer(User user) {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM timers WHERE userId = ?");
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("timersId");
                int userId = resultSet.getInt("userId");
                int workDuration = resultSet.getInt("workDuration");
                int shortBreakDuration = resultSet.getInt("shortBreakDuration");
                int longBreakDuration = resultSet.getInt("longBreakDuration");
                int longBreakAfter = resultSet.getInt("longBreakAfter");
                Timer timer = new Timer(workDuration, shortBreakDuration, longBreakDuration, longBreakAfter);
                timer.setId(id);
                return timer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no timer is found for the user
    }

    /**
     * Creates a new timer for the user if it doesn't already exist.
     * @param user The user to create a timer for.
     */
    @Override
    public void createUserTimer(User user){
        // Check if the user already has a timer
        if (getUserTimer(user) != null) {
            System.out.println("User already has a timer.");
        } else {
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
                    System.out.println("User's timer created successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
