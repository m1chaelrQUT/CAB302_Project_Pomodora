package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import com.qut.cab302_project_pomodora.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
import java.sql.SQLException;

// Extend the abstract skeleton
public class SigninController extends ControllerSkeleton {

    // FXML ids specific to the Signin Controller
    @FXML private StackPane signIn;
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
    @FXML private AnchorPane usernameFailPrompt;
    @FXML private AnchorPane passwordFailPrompt;
    @FXML private Label failText;
    @FXML private AnchorPane emailResetFail;

    // User DAO interface
    private IUserDAO userDAO;

    private Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();

    public SigninController() {
        userDAO = new SqliteUserDAO();
    }

    // Implement the abstract methods to provide the required containers : This is for common scaling
    @Override
    protected StackPane getRootPane() {
        return signIn;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    // Init
    @Override
    @FXML
    public void initialize() throws SQLException, IOException {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("SigninController Initialization completed.");
    }

    // Sign-In
    @FXML
    private void handleSignIn() throws IOException, SQLException {
        // Get sign in inputs
        String userNameInput = usernameField.getText();
        String passwordInput = passwordField.getText();

        boolean credentialsFilled = (userNameInput != null && passwordInput != null && !userNameInput.isEmpty() && !passwordInput.isEmpty());
        if (credentialsFilled) {
            // Check if user exists
            User user = userDAO.getUserByName(userNameInput);
            if (user == null){
                user = userDAO.getUserByEmail(userNameInput);
            }

            // Check if password is correct
            if ((user != null) && (user.getPassword().equals(passwordInput))) {
                // Start session
                SessionManager.startSession(user);
                String tempToken = user.getSessionToken();
                System.out.println("Sign-in successful! User: " + userNameInput + ".");

                // TODO: Navigate through to Home Screen.
                usernameFailPrompt.setVisible(false);
                passwordFailPrompt.setVisible(false);
                navigateTo("homeexample");
            } else {
                System.out.println("Username not found");
                failText.setText("The username or password is incorrect.");
                usernameFailPrompt.setVisible(true);
                passwordFailPrompt.setVisible(true);
            }
        } else {
            failText.setText("Please fill any fields marked with *");
            usernameFailPrompt.setVisible(true);
            passwordFailPrompt.setVisible(true);
        }
    }

    @FXML
    private void showResetDialog() {
        System.out.println("SigninController showResetDialog");
        resetPane.setVisible(true);
    }
    private void showSuccessfulResetDialog() {
        System.out.println("SigninController showResetDialog");
        resetSuccessPane.setVisible(true);
    }

    @FXML
    private void goToSignUp() throws IOException {
        System.out.println("SigninController gotoSignUp");
        navigateTo("signup");
    }

    @FXML
    private void handleResetPassword() {
        String enteredEmail = usernameFieldReset.getText();
        boolean validEmail = enteredEmail.contains("@");
        System.out.println("SigninController handleResetPassword");
        System.out.println("Username Entered: " + enteredEmail);
        if (validEmail) {
            emailResetFail.setVisible(false);
            closeForgotPasswordDialog();
            showSuccessfulResetDialog();
        }else {
            failText.setText("Please enter a valid email address.");
            emailResetFail.setVisible(true);
        }
    }

    @FXML
    private void closeForgotPasswordDialog() {
        System.out.println("SigninController closeForgotPasswordDialog");

        resetPane.setVisible(false);
        usernameFieldReset.setText("");
        emailResetFail.setVisible(false);

    }

    @FXML
    private void closeSuccessDialog() {
        System.out.println("SigninController closeSuccessDialog");
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