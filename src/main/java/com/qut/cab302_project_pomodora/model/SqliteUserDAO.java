package com.qut.cab302_project_pomodora.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

public class SqliteUserDAO implements IUserDAO {
    private Connection connection;

    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();

        // If table doesn't exist
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userName VARCHAR NOT NULL,"
                    + "password VARCHAR NOT NULL,"
                    + "playerLevel INTEGER NOT NULL,"
                    + "levelExp INTEGER NOT NULL,"
                    + "email VARCHAR NOT NULL,"
                    + "createdAt DATE NOT NULL,"
                    + "updatedAt DATE NOT NULL,"
                    + "sessionToken VARCHAR"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users " +
                    "(userName, password, playerLevel, levelExp, email, createdAt, updatedAt, sessionToken) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getPlayerLevel());
            statement.setInt(4, user.getLevelExp());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getCreatedAt().toString());
            statement.setString(7, user.getUpdatedAt().toString());
            statement.setString(8, user.getSessionToken());

            // Execute Insert Query
            int rowsAffected = statement.executeUpdate();

            // If the insert was successful, assign the AI id to the user
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Set the generated id in the user object
                    user.setId(generatedKeys.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method updating user information
     * @param user - the user to be deleted
     */
    @Override
    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET email = ?, password = ? WHERE userName = ?");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserName());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully: " + user.getUserName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for getting users info using username
     * @param - the userName of the user
     * @return the user's username, level, exp
     */
    @Override
    public User getUserByName(String userName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE userName = ?");
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                int playerLevel = resultSet.getInt("playerLevel");
                int levelExp = resultSet.getInt("levelExp");
                String email = resultSet.getString("email");
                User user = new User(userName, password, playerLevel, levelExp, email);
                user.setId(id);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method for getting user by the email
     * @param userEmail The email of the user to retrieve
     * @return
     */
    @Override
    public User getUserByEmail(String userEmail) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                int playerLevel = resultSet.getInt("playerLevel");
                int levelExp = resultSet.getInt("levelExp");
                userEmail = resultSet.getString("email");
                User user = new User(userName, password, playerLevel, levelExp, userEmail);
                user.setId(id);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
