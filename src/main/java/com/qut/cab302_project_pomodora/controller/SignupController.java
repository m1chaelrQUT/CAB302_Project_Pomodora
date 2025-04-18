package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.IUserDAO;
import com.qut.cab302_project_pomodora.Main;
import com.qut.cab302_project_pomodora.MockUserDAO;
import com.qut.cab302_project_pomodora.User;
import com.qut.cab302_project_pomodora.config.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

// Extend the abstract skeleton
public class SignupController extends ControllerSkeleton {

    // FXML ids specific to the Home Controller
    @FXML private StackPane rootPane;
    @FXML private StackPane contentPane;


    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Region topSpacer;
    @FXML private Region bottomSpacer;
    @FXML private Label brandLabel;
    @FXML private VBox signinPane;
    @FXML private Label ctaLabel;
    @FXML private Label ctaLogo;
    @FXML private StackPane resetPane;
    @FXML private StackPane resetSuccessPane;
    @FXML private TextField usernameFieldReset;

    private IUserDAO userDAO;

    private Theme currentTheme = Theme.LIGHT;

    public SignupController() {
        // Load mock users for testing
        userDAO = new MockUserDAO();
        // TODO: Swap out Mock for DB implementation SQLiteUserDAO
    }

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

    // Sign-Up
    // TODO: Clean up commented code
    @FXML
    private void handleSignUp() {
        // Get sign in inputs
        String userNameInput = usernameField.getText();
        String passwordInput = passwordField.getText();

        // Check if existing user is input
        User user = userDAO.getUser(userNameInput);
        if(user != null) {
            System.out.println("There already exists a user with that username, please try a different one.");
            // TODO: Output error message to user through the GUI.
        } else {
            // Default Values for new User - Starting Level
            final int DEFAULT_PLAYER_LEVEL = 1;
            final int DEFAULT_LEVEL_EXPERIENCE = 0;

            // Add the new user
            User newUser = new User(userNameInput, passwordInput, DEFAULT_PLAYER_LEVEL, DEFAULT_LEVEL_EXPERIENCE);
            userDAO.addUser(newUser);

            // TODO: Navigate through to Home Screen.
        }

        //Debugging
        // Check that new user was correctly stored
        //System.out.println(MockUserDAO.users);

//        System.out.println("HomeController handleSignIn");
//        System.out.println("Username Entered: " + usernameField.getText());
//        System.out.println("Password Entered: " + passwordField.getText());
//        System.out.println("Remember Me? " + rememberMeCheckBox.isSelected());

        // Hijacked to demo ThemeManager util
//        if (currentTheme == Theme.LIGHT) {
//            currentTheme = Theme.DARK;
//        } else if (currentTheme == Theme.DARK) {
//            currentTheme = Theme.VOIDLIGHT;
//        } else {currentTheme = Theme.LIGHT;}
//        changeTheme(currentTheme);
    }

    @FXML
    private void handleSignIn() throws IOException {
        System.out.println("HomeController handleSignUp");
        // TEMP - TODO: Change to sign up page
        Scene currentScene = (Scene) signinPane.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/qut/cab302_project_pomodora/fxml/signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(scene);
    }

}