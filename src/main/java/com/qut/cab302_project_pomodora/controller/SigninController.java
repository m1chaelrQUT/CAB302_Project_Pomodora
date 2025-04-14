package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

// Extend the abstract skeleton
public class SigninController extends ControllerSkeleton {

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

    private Theme currentTheme = Theme.LIGHT;


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
        // Hijacked to demo ThemeManager util
        if (currentTheme == Theme.LIGHT) {
            currentTheme = Theme.DARK;
        } else if (currentTheme == Theme.DARK) {
            currentTheme = Theme.VOIDLIGHT;
        } else {currentTheme = Theme.LIGHT;}
        changeTheme(currentTheme);
    }

    @FXML
    private void showResetDialog() {
        System.out.println("HomeController showResetDialog");
        // TEMP - TODO: Open password reset popup
        resetPane.setVisible(true);
    }
    private void showSuccessfulResetDialog() {
        System.out.println("HomeController showResetDialog");
        // TEMP - TODO: Open password reset popup
        resetSuccessPane.setVisible(true);
    }

    @FXML
    private void handleSignUp() throws IOException {
        System.out.println("HomeController handleSignUp");
        // TEMP - TODO: Change to sign up page
        Scene currentScene = (Scene) signinPane.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/qut/cab302_project_pomodora/fxml/signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(scene);
    }

    @FXML
    private void handleResetPassword() {
        System.out.println("HomeController handleResetPassword");
        System.out.println("Username Entered: " + usernameFieldReset.getText());
        Boolean validEmail = true;
        if (validEmail) {
            closeForgotPasswordDialog();
            showSuccessfulResetDialog();
        }
    }

    @FXML
    private void closeForgotPasswordDialog() {
        System.out.println("HomeController closeForgotPasswordDialog");

        resetPane.setVisible(false);
        usernameFieldReset.setText("");

    }

    @FXML
    private void closeSuccessDialog() {
        System.out.println("HomeController closeSuccessDialog");
        resetSuccessPane.setVisible(false);
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