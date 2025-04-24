package com.qut.cab302_project_pomodora.model;


import java.time.LocalDateTime;

public class User {
    private int id;
    private String userName;
    private String password;
    private int playerLevel;
    private int levelExp;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public User(String userName, String password, int playerLevel, int levelExp, String email){
        this.userName = userName;
        this.password = password;
        this.playerLevel = playerLevel;
        this.levelExp = levelExp;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Todo: Do we really need the following fields?
//    private String rememberMeToken;


    /**
     * Getters and Setters of Variables
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getLevelExp() {
        return levelExp;
    }

    public void setLevelExp(int levelExp) {
        this.levelExp = levelExp;
    }

    // String conversion for debugging
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

}

