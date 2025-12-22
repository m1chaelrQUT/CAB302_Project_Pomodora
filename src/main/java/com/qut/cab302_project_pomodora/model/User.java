package com.qut.cab302_project_pomodora.model;
import java.time.LocalDateTime;

/**
 * Represents a user in the application.
 * A user contains information about their ID, username, password, player level, experience points,
 * email, creation date, update date, and session token.
 */
public class User {
    private int id;
    private String userName;
    private String password;
    private int playerLevel;
    private int levelExp;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sessionToken;


    /**
     * Default constructor for User.
     * Initializes a new instance of the User class.
     * @param userName the username
     * @param password the password
     * @param playerLevel the player level
     * @param levelExp the level experience points
     * @param email the email address
     */
    public User(String userName, String password, int playerLevel, int levelExp, String email){
        this.userName = userName;
        this.password = password;
        this.playerLevel = playerLevel;
        this.levelExp = levelExp;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor for User.
     * @param id the user ID
     * @param userName the username
     * @param playerLevel the player level
     * @param levelExp the level experience points
     */
    public User(int id, String userName, int playerLevel, int levelExp) {
        this.id = id;
        this.userName = userName;
        this.playerLevel = playerLevel;
        this.levelExp = levelExp;
    }

    /**
     * Getter for the user ID.
     * @return the user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the user ID.
     * @param id the user ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the username.
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for the username.
     * @param userName the username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for the password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the email address.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email address.
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the creation date.
     * @return the creation date
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter for the update date.
     * @return the update date
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the update date.
     * @param updatedAt the update date
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the player level.
     * @return the player level
     */
    public int getPlayerLevel() {
        return playerLevel;
    }

    /**
     * Setter for the player level.
     * @param playerLevel the player level
     */
    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    /**
     * Getter for the level experience points.
     * @return the level experience points
     */
    public int getLevelExp() {
        return levelExp;
    }

    /**
     * Setter for the level experience points.
     * @param levelExp the level experience points
     */
    public void setLevelExp(int levelExp) {
        this.levelExp = levelExp;
    }

    /**
     * Getter for the session token.
     * @return the session token
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Setter for the session token.
     * @param sessionToken the session token
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * Convert the user object into a string representaion
     * @return a string represenation of the user object
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", playerLevel=" + playerLevel +
                ", levelExp=" + levelExp +
                ", email='" + email + '\'' +
                '}';
    }

    public boolean login(String emailInput, String passwordInput) {
        return this.email.equals(emailInput) && this.password.equals(passwordInput);
    }
}

