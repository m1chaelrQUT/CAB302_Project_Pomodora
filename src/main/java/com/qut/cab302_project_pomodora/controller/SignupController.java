package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.model.*;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

/**
 * SignupController is responsible for managing the sign-up view in the application.
 * It handles user registration and navigation to other views.
 */
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
    private ITimerDAO timerDAO;

    /**
     * Constructor for SignupController.
     * Initializes the userDAO and timerDAO to interact with the database.
     */
    public SignupController() {
        userDAO = new SqliteUserDAO();
        timerDAO = new SqliteTimerDAO();
    }

    /**
     * Gets the root pane of the sign-up view.
     * @return the root pane of the sign-up view.
     */
    @Override
    protected StackPane getRootPane() {
        return signUp;
    }

    /**
     * Gets the content pane of the sign-up view.
     * @return the content pane of the sign-up view.
     */
    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    /**
     * Initializes the sign-up view.
     * Sets the preferred size of the content pane and initializes the theme.
     * @throws SQLException if there is an error with the database connection.
     * @throws IOException if there is an error with the FXML file.
     */
    @Override
    @FXML
    public void initialize() throws SQLException, IOException {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("SignupController Initialization completed.");
    }


    /**
     * Handles the sign-up process when the sign-up button is clicked.
     * Validates user input and creates a new user if the input is valid.
     */
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

                // Initialize the User's timer
                timerDAO.createUserTimer(newUser);
                showSuccessDialog();

            }
        } else {
            failText.setText("Please fill any fields marked with *");
            usernameFailPrompt.setVisible(true);
            emailFailPrompt.setVisible(true);
            passwordFailPrompt.setVisible(true);
        }
    }

    /**
     * Go to the sign-in page when the "Already have an account?" button is clicked.
     * This method is called when the button is clicked.
     * @throws IOException if there is an error with the FXML file.
     */
    @FXML
    private void goToSignIn() throws IOException {
        System.out.println("SignupController goToSignup");
        navigateTo("signin");
    }

    /**
     * shows the success dialog when the sign-up is successful.
     */
    @FXML
    private void showSuccessDialog() {
        successDialog.setVisible(true);
    }

    /**
     * closes the success dialog when the "Sign In" button is clicked.
     */
    @FXML
    private void closeSuccessDialog() {
        successDialog.setVisible(false);
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    /**
     * Handles the sign-in process when the "Sign In" button is clicked.
     * Navigates to the home page after successful sign-in.
     * @throws SQLException if there is an error with the database connection.
     * @throws IOException if there is an error with the FXML file.
     */
    @FXML
    private void handleSignIn() throws SQLException, IOException {
        String userNameInput = usernameField.getText();

        // Start session for the newly created user, then navigate to home page
        SessionManager.startSession(userDAO.getUserByName(userNameInput));
        navigateTo("studyplanners");
    }

    /**
     * get support when the "Get Support" button is clicked.
     * This method is called when the button is clicked.
     */
    @FXML
    private void getSupport() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=xvFZjo5PgG0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}