package com.qut.cab302_project_pomodora;


import javafx.scene.control.PasswordField;

import java.util.Date;

public class User {
    private int id;
    private String userName;
    private String password;
    private int playerLevel;
    private int levelExp;

    public User(String userName, String password, int playerLevel, int levelExp){
        this.userName = userName;
        this.password = password;
        this.playerLevel = playerLevel;
        this.levelExp = levelExp;
    }

    // Todo: Do we really need the following fields?
//    private String email;
//    private String rememberMeToken;
//    private Date createdAt;
//    private Date updatedAt;

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

    // String conversion for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", playerLevel=" + playerLevel +
                ", levelExp=" + levelExp +
                '}';
    }

}

