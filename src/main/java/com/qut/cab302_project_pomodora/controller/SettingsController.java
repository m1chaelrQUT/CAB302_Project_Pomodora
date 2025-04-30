package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

public class SettingsController extends ControllerSkeleton {
    @FXML
    private StackPane settings;
    @FXML
    private StackPane contentPane;
    @FXML
    private StackPane timerSettingsPopUp;
    @FXML
    private StackPane accountSettingsPopUp;

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;
    // Input fields for Timer Settings
    @FXML
    private Spinner pomodoroMinutesSpinner;
    @FXML
    private Spinner pomodoroSecondsSpinner;
    @FXML
    private Spinner shortBreakMinutesSpinner;
    @FXML
    private Spinner shortBreakSecondsSpinner;
    @FXML
    private Spinner longBreakMinutesSpinner;
    @FXML
    private Spinner longBreakSecondsSpinner;
    @FXML
    private Spinner longBreakCyclesSpinner;

    // Input fields for Account Settings
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField newPasswordEntryField;
    @FXML
    private PasswordField confirmNewPasswordEntryField;

    // DAO interfaces
    private IUserDAO userDAO;
    private ITimerDAO timerDAO;

    // Current user object
    private User currentUser;

    public SettingsController() {
        userDAO = new SqliteUserDAO();
        timerDAO = new SqliteTimerDAO();
    }

    // Open/Show function for the pop-ups/overlays (Stackpanes)
    @FXML
    private void openTimerSettings() {
        timerSettingsPopUp.setVisible(true);
    }

    @FXML
    private void openAccountSettings() {
        accountSettingsPopUp.setVisible(true);
    }

    // Close function for the pop-ups/overlays (Stackpanes)
    private void closePopUp(StackPane popUp) {
        popUp.setVisible(false);
    }

    @FXML
    private void closeTimerSettings() {
        closePopUp(timerSettingsPopUp);
    }

    @FXML
    private void closeAccountSettings() {
        closePopUp(accountSettingsPopUp);
    }


    @Override
    protected StackPane getRootPane() {
        return settings;
    }

    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    @Override
    @FXML
    public void initialize() throws SQLException, IOException {
        super.initialize();
        iniSession();

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(settings.getScene());
        });

        System.out.println("SettingsController Initialization completed.");
    }

    // TODO: Add the iniSession() method to load the session and get the user
    /**
     * Initializes the session by loading the current user from the session manager.
     * This method is called during the initialization of the controller.
     *
     * @throws SQLException if there is an error loading the session from the database
     * @throws IOException  if there is an error loading the session from the file
     */
    public void iniSession() throws SQLException, IOException {
        // Load the session to check if the user is already logged in
        SessionManager.loadSession();

        currentUser = SessionManager.getCurrentUser();
        System.out.println("Session loaded!");
    }

    /* Account Settings Pop Up Methods*/
    //@FXML
//    private PasswordField newPasswordEntryField;

    @FXML
    private TextField newPasswordTextField;

    @FXML Button showNewPasswordButton;


//    @FXML
//    private PasswordField confirmNewPasswordEntryField;

    @FXML
    private TextField confirmNewPasswordTextField;

    @FXML
    private Button showConfirmNewPasswordButton;


    private boolean isSetPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @FXML
    private void toggleSetPasswordVisibility() {
        // Flip the bool value
        isSetPasswordVisible = !isSetPasswordVisible;

        if (isSetPasswordVisible) {
            // Gets the entry in the password field
            newPasswordTextField.setText(newPasswordEntryField.getText());
            // Makes the text field visible
            newPasswordTextField.setVisible(true);
            newPasswordTextField.setManaged(true);
            // Hides the password field
            newPasswordEntryField.setVisible(false);
            newPasswordEntryField.setManaged(false);
        } else {
            // Whatever is entered in the text field will now go the password field
            newPasswordEntryField.setText(newPasswordTextField.getText());
            newPasswordEntryField.setVisible(true);
            newPasswordEntryField.setManaged(true);
            newPasswordTextField.setVisible(false);
            newPasswordTextField.setManaged(false);
        }
    }

    @FXML
    private void toggleConfirmNewPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible;

        if (isConfirmPasswordVisible) {
            // Gets the entry in the password field
            confirmNewPasswordTextField.setText(confirmNewPasswordEntryField.getText());
            // Makes the text field visible
            confirmNewPasswordTextField.setVisible(true);
            confirmNewPasswordTextField.setManaged(true);
            // Hides the password field
            confirmNewPasswordEntryField.setVisible(false);
            confirmNewPasswordEntryField.setManaged(false);
        } else {
            // Whatever is entered in the text field will now go the password field
            confirmNewPasswordEntryField.setText(confirmNewPasswordTextField.getText());
            confirmNewPasswordEntryField.setVisible(true);
            confirmNewPasswordEntryField.setManaged(true);
            confirmNewPasswordTextField.setVisible(false);
            confirmNewPasswordTextField.setManaged(false);
        }
    }

    public String getSetPassword() {
        return isSetPasswordVisible ? newPasswordTextField.getText() : newPasswordEntryField.getText();
    }

    public String getConfirmPassword() {
        return isConfirmPasswordVisible ? confirmNewPasswordTextField.getText() : confirmNewPasswordEntryField.getText();
    }

    /**
     * This method is called when the user clicks the "Confirm" button in the account settings pop-up.
     * It updates the user's email and password in the database.
     */
    @FXML
    private void confirmAccountUpdate() {
        String emailInput = emailTextField.getText();
        String newPasswordInput = getSetPassword();
        String confirmNewPasswordInput = getConfirmPassword();

        // Update email if the field is not empty
        if (!emailInput.isEmpty()) {
            currentUser.setEmail(emailInput);
        }

        // Update password if both fields are not empty and match
        if (!newPasswordInput.isEmpty() && !confirmNewPasswordInput.isEmpty()) {
            if (newPasswordInput.equals(confirmNewPasswordInput)) {
                currentUser.setPassword(newPasswordInput);
                System.out.println("New password entered: " + newPasswordInput);
            } else {
                System.out.println("Error, password and confirm password do not match.");
                return;
            }
        } else if (newPasswordInput.isEmpty() && confirmNewPasswordInput.isEmpty()) {
            System.out.println("Password fields are empty, password unchanged.");
        } else {
            System.out.println("Error, incomplete password fields.");
            return;
        }

        // Update the user in the database
        userDAO.updateUser(currentUser);

        //TODO: [BackEnd] Update updatedAt field for user

        //TODO: [FrontEnd] Display success message to user
    }

    @FXML
    private void saveTimerSettings() {

        //TODO: Check if no timer settings are set, if so then initialize them
        Timer currentUserTimer = timerDAO.getUserTimer(currentUser);

        int pomodoroMinutesInput = Integer.parseInt(pomodoroMinutesSpinner.getValue().toString());
        int pomodoroSecondsInput = Integer.parseInt(pomodoroSecondsSpinner.getValue().toString());
        int shortBreakMinutesInput = Integer.parseInt(shortBreakMinutesSpinner.getValue().toString());
        int shortBreakSecondsInput = Integer.parseInt(shortBreakSecondsSpinner.getValue().toString());
        int longBreakMinutesInput = Integer.parseInt(longBreakMinutesSpinner.getValue().toString());
        int longBreakSecondsInput = Integer.parseInt(longBreakSecondsSpinner.getValue().toString());
        int longBreakCyclesInput = Integer.parseInt(longBreakCyclesSpinner.getValue().toString());

        currentUserTimer.setWorkDuration((pomodoroMinutesInput * 60) + pomodoroSecondsInput);
        currentUserTimer.setShortBreakDuration((shortBreakMinutesInput * 60) + shortBreakSecondsInput);
        currentUserTimer.setLongBreakDuration((longBreakMinutesInput * 60) + longBreakSecondsInput);
        currentUserTimer.setLongBreakAfter(longBreakCyclesInput);

        // Update the user's timer settings
        timerDAO.updateUserTimers(currentUser, currentUserTimer);
    }
}

