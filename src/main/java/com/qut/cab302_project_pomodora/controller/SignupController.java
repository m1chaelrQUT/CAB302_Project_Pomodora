package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.IUserDAO;
import com.qut.cab302_project_pomodora.Main;
import com.qut.cab302_project_pomodora.model.MockUserDAO;
import com.qut.cab302_project_pomodora.model.SqliteUserDAO;
import com.qut.cab302_project_pomodora.model.User;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

// Extend the abstract skeleton
public class SignupController extends ControllerSkeleton {

    // FXML ids specific to the Signup Controller
    @FXML private StackPane signUp;
    @FXML private StackPane contentPane;


    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox loginCheckBox;
    @FXML private Region topSpacer;
    @FXML private Region bottomSpacer;
    @FXML private Label brandLabel;
    @FXML private VBox signupPane;
    @FXML private Label ctaLabel;
    @FXML private Label ctaLogo;
    @FXML private StackPane resetPane;
    @FXML private StackPane resetSuccessPane;
    @FXML private TextField usernameFieldReset;
    @FXML private AnchorPane usernameFailPrompt;
    @FXML private AnchorPane emailFailPrompt;
    @FXML private AnchorPane passwordFailPrompt;
    @FXML private Label failText;
    @FXML private StackPane successDialog;

    private Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();

    private IUserDAO userDAO;

    public SignupController() {
        userDAO = new SqliteUserDAO();
    }

    // Implement the abstract methods to provide the required containers : This is for common scaling
    @Override
    protected StackPane getRootPane() {
        return signUp;
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

        System.out.println("SignupController Initialization completed.");
    }

    // Sign-Up
    @FXML
    private void handleSignUp() {
        // Get sign in inputs
        String userNameInput = usernameField.getText();
        String emailInput = emailField.getText();
        String passwordInput = passwordField.getText();

        boolean credentialsFilled = (userNameInput != null && passwordInput != null && !userNameInput.isEmpty() && !passwordInput.isEmpty());
        // Check if existing user is input
        if (credentialsFilled) {
            User userName = userDAO.getUserByName(userNameInput);
            User userEmail = userDAO.getUserByEmail(emailInput);
            if (!emailInput.contains("@")) {
                System.out.println("Invalid email address");
                failText.setText("Invalid email address");
                usernameFailPrompt.setVisible(true);
                emailFailPrompt.setVisible(true);
                passwordFailPrompt.setVisible(true);

            } else if ((userName != null) || (userEmail != null)) {
                System.out.println("There already exists a user with that username or email, please try a different one.");
                failText.setText("Username or email not available.");
                usernameFailPrompt.setVisible(true);
                emailFailPrompt.setVisible(true);
                passwordFailPrompt.setVisible(true);
            } else {
                usernameFailPrompt.setVisible(false);
                emailFailPrompt.setVisible(false);
                passwordFailPrompt.setVisible(false);
                // Default Values for new User - Starting Level
                final int DEFAULT_PLAYER_LEVEL = 1;
                final int DEFAULT_LEVEL_EXPERIENCE = 0;

                // Add the new user
                User newUser = new User(userNameInput, passwordInput, DEFAULT_PLAYER_LEVEL, DEFAULT_LEVEL_EXPERIENCE, emailInput);
                System.out.println("New user: " + newUser);
                userDAO.addUser(newUser);
                showSuccessDialog();

                // TODO: Navigate through to Home Screen.
            }
        } else {
            failText.setText("Please fill any fields marked with *");
            usernameFailPrompt.setVisible(true);
            emailFailPrompt.setVisible(true);
            passwordFailPrompt.setVisible(true);
        }
    }

    @FXML
    private void goToSignIn() throws IOException {
        System.out.println("SignupController goToSignup");
        navigateTo("signin");
    }

    @FXML
    private void showSuccessDialog() {
        successDialog.setVisible(true);
    }

    @FXML
    private void closeSuccessDialog() {
        successDialog.setVisible(false);
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleSignIn() {
        String userNameInput = usernameField.getText();
        //This should ideally be the exact same logic as regular signin. TODO: Create parent controller class specific to sign-in-up
    }

    @FXML
    private void getSupport() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=xvFZjo5PgG0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}