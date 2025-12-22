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

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

/**
 * SigninController is responsible for managing the sign-in view in the application.
 * It handles user authentication and navigation to other views.
 */
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

    /**
     * Constructor for SigninController.
     * Initializes the userDAO to interact with the database.
     */
    public SigninController() {
        userDAO = new SqliteUserDAO();
    }

    /**
     * Gets the root pane of the sign-in view.
     * @return the root pane of the sign-in view.
     */
    @Override
    protected StackPane getRootPane() {
        return signIn;
    }

    /**
     * Gets the content pane of the sign-in view.
     * @return the content pane of the sign-in view.
     */
    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    /**
     * Initializes the sign-in view.
     * Sets the preferred size and visibility of various elements.
     * @throws SQLException throws SQLException if there is an error with the database connection.
     * @throws IOException throws IOException if there is an error with the FXML file.
     */
    @Override
    @FXML
    public void initialize() throws SQLException, IOException {
        super.initialize();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("SigninController Initialization completed.");
    }

    /**
     * Handles the sign-in process when the sign-in button is clicked.
     * Validates the user credentials and starts a session if successful.
     * @throws IOException throws IOException if there is an error with the FXML file.
     * @throws SQLException throws SQLException if there is an error with the database connection.
     */
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
                System.out.println("Sign-in successful! User: " + userNameInput + ".");

                // TODO: Navigate through to Home Screen.
                usernameFailPrompt.setVisible(false);
                passwordFailPrompt.setVisible(false);
                navigateTo("studyplanners");
            } else {
                System.out.println("The username or password is incorrect.");
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

    /**
     * Shows the reset password dialog when the "Forgot Password" button is clicked.
     */
    @FXML
    private void showResetDialog() {
        System.out.println("SigninController showResetDialog");
        resetPane.setVisible(true);
    }

    /**
     * Shows the success dialog when the password reset is successful.
     * This method is called after a successful password reset.
     */
    private void showSuccessfulResetDialog() {
        System.out.println("SigninController showResetDialog");
        resetSuccessPane.setVisible(true);
    }

    /**
     * Go toes to the sign-up page when the "Sign Up" button is clicked.
     * @throws IOException throws IOException if there is an error with the FXML file.
     */
    @FXML
    private void goToSignUp() throws IOException {
        System.out.println("SigninController gotoSignUp");
        navigateTo("signup");
    }

    /**
     * Handles the reset password process when the "Reset Password" button is clicked.
     * Validates the email address and shows a success dialog if valid.
     */
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

    /**
     * Closes the reset password dialog when the "Close" button is clicked.
     * Resets the email field and hides the error message.
     */
    @FXML
    private void closeForgotPasswordDialog() {
        System.out.println("SigninController closeForgotPasswordDialog");

        resetPane.setVisible(false);
        usernameFieldReset.setText("");
        emailResetFail.setVisible(false);

    }

    /**
     * Closes the success dialog when the "Close" button is clicked.
     * Hides the success pane.
     */
    @FXML
    private void closeSuccessDialog() {
        System.out.println("SigninController closeSuccessDialog");
        resetSuccessPane.setVisible(false);
    }

    /**
     * Opens the support page when the "Get Support" button is clicked.
     * This method opens a web browser to the specified URL.
     */
    @FXML
    private void getSupport() {
        try {
            //Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=xvFZjo5PgG0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}