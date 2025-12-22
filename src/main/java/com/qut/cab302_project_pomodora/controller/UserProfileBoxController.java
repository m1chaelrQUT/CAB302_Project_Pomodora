package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.SessionManager;
import com.qut.cab302_project_pomodora.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UserProfileBoxController {
    // FXML elements for user-profile-nobar
    @FXML private AnchorPane userProfilePaneNoBar;
    @FXML private Label usernameLabelNoBar;
    @FXML private ImageView userIndicatorIconNoBar;


    public void initialize() {
        setUsernameLabel();
    }

    private void setUsernameLabel() {
        User currentUser = SessionManager.getCurrentUser();
        if(currentUser != null){
            usernameLabelNoBar.setText(currentUser.getUserName());
        } else {
            usernameLabelNoBar.setText("Guest");
        }
    }
}
