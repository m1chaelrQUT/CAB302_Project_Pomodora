package com.qut.cab302_project_pomodora.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

// Extend the abstract skeleton
public class HomeController extends ControllerSkeleton {

    // FXML ids specific to the Home Controller
    @FXML private StackPane rootPane;
    @FXML private VBox contentPane;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Region topSpacer;
    @FXML private Region bottomSpacer;
    @FXML private Label brandLabel;
    @FXML private VBox signinPane;
    @FXML private Label ctaLabel;
    @FXML private Label ctaLogo;



    // Implement the abstract methods to provide the required containers : This is for common scaling
    @Override
    protected StackPane getRootPane() {
        return rootPane;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    // Init
    @Override
    @FXML
    public void initialize() {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("HomeController Initialization completed.");
    }

    // Signin
    @FXML
    private void handleSignIn() {
        System.out.println("HomeController handleSignIn");
        System.out.println("Username Entered: " + usernameField.getText());
        System.out.println("Password Entered: " + passwordField.getText());
        System.out.println("Remember Me? " + rememberMeCheckBox.isSelected());
        // TEMP - TODO : Real Signin Logic
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("HomeController handleForgotPassword");
        // TEMP - TODO: Open password reset popup
    }

    @FXML
    private void handleSignUp() {
        System.out.println("HomeController handleSignUp");
        // TEMP - TODO: Change to sign up page
    }

}