package com.qut.cab302_project_pomodora.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SessionManager {
    private static final String SESSION_FILE = "session.txt"; // Path to the session file
    private static User currentUser = null;

    public static void startSession(User user) throws SQLException, IOException {
        System.out.println("Starting session for user: " + user.getUserName());

        // Generate a new session token
        String sessionToken = UUID.randomUUID().toString();

        // Update the session token in the database
        Connection connection = SqliteConnection.getInstance();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET sessionToken = ? WHERE userName = ?"
        );
        statement.setString(1, sessionToken);
        statement.setString(2, user.getUserName());
        statement.executeUpdate();

        // Save the session token to a file
        Files.write(Paths.get(SESSION_FILE), sessionToken.getBytes());
        currentUser = user;
        currentUser.setSessionToken(sessionToken);
    }

    public static void loadSession() throws SQLException, IOException{
        // Check if the session file exists
        System.out.println("Checking if session file exists...");
        if (Files.exists(Paths.get(SESSION_FILE))) {
            // Example logic to load session
            System.out.println("Session file exists! Loading session...");
            // Read the session token from the file
            String sessionToken = new String(Files.readAllBytes(Paths.get(SESSION_FILE)));

            // Query the database for the user with the session token
            Connection connection = SqliteConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE sessionToken = ?"
            );
            statement.setString(1, sessionToken);
            ResultSet resultSet = statement.executeQuery();

            // If a user is found, create a User object and set it as the current user
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("userName");
                int playerLevel = resultSet.getInt("playerLevel");
                int levelExp = resultSet.getInt("levelExp");
                currentUser = new User(id, userName, playerLevel, levelExp);
                currentUser.setSessionToken(sessionToken);
            } else {
                System.out.println("No user found with the session token.");

                // If no user is found, delete the session file
                Files.deleteIfExists(Paths.get(SESSION_FILE));
            }
        } else {
            System.out.println("Session file does not exist.");
            currentUser = null; // No session to load
        }
    }

    public static void endSession() throws SQLException, IOException {
        // Check if the current user is not null
        if (currentUser != null) {
            // Remove the session token from the database
            Connection connection = SqliteConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET sessionToken = NULL WHERE userName = ?"
            );
            statement.setString(1, currentUser.getSessionToken());
            statement.executeUpdate();

            Files.deleteIfExists(Paths.get(SESSION_FILE)); // Delete the session file
            currentUser = null; // Clear the current user
        }
    }

    // Getters and Setters
    public static User getCurrentUser() {
        // Check if the current user is not null
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return null;
        // If the user is logged in, return the current user
        } else {
            System.out.println("Returning current user: " + currentUser.getUserName());
            return currentUser;
        }
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
