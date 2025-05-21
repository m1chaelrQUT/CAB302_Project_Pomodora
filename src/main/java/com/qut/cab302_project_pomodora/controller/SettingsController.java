package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.*;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.util.ThemeManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

/**
 * SettingsController is responsible for managing the settings view in the application.
 * It handles the display and modification of user settings, including timer settings, account settings, and theme settings.
 */
public class SettingsController extends ControllerSkeleton {
    @FXML
    private StackPane settings;
    @FXML
    private StackPane contentPane;
    @FXML
    private StackPane timerSettingsPopUp;
    @FXML
    private StackPane accountSettingsPopUp;
    @FXML
    private StackPane themeSettingsPopUp;

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

    @FXML private ChoiceBox<Theme> colourSchemeSelector;

    enum Timers {
        BAR, ANALOG, DIGITAL
    }

    @FXML private ChoiceBox<Timers> timerUISelector;

    // DAO interfaces
    private IUserDAO userDAO;
    private ITimerDAO timerDAO;

    // Current user object
    private User currentUser;

    /**
     * Constructor for SettingsController.
     * Initializes the userDAO and timerDAO objects.
     */
    public SettingsController() {
        userDAO = new SqliteUserDAO();
        timerDAO = new SqliteTimerDAO();
    }

    /**
     * Opens the timer settings pop-up.
     * This method is called when the user clicks the "Timer Settings" button.
     */
    @FXML
    private void openTimerSettings() {
        timerSettingsPopUp.setVisible(true);
        settingsSavedLabel.setVisible(false);

        // Load current timer settings
        Timer currentTimerSettings = timerDAO.getUserTimer(currentUser);
        if (currentTimerSettings != null) {
            int workDuration = currentTimerSettings.getWorkDuration();
            int shortBreak = currentTimerSettings.getShortBreakDuration();
            int longBreak = currentTimerSettings.getLongBreakDuration();
            int longBreakAfter = currentTimerSettings.getLongBreakAfter();

            // Set values into spinners
            // Total time stored in seconds, so value / 60 = nearest int
            pomodoroMinutesSpinner.getValueFactory().setValue(workDuration / 60);
            // Value % 60 = remainder, i.e., seconds after whole minutes calculated
            pomodoroSecondsSpinner.getValueFactory().setValue(workDuration % 60);

            shortBreakMinutesSpinner.getValueFactory().setValue(shortBreak / 60);
            shortBreakSecondsSpinner.getValueFactory().setValue(shortBreak % 60);

            longBreakMinutesSpinner.getValueFactory().setValue(longBreak / 60);
            longBreakSecondsSpinner.getValueFactory().setValue(longBreak % 60);

            longBreakCyclesSpinner.getValueFactory().setValue(longBreakAfter);
        }
    }

    /**
     * Opens the account settings pop-up.
     * This method is called when the user clicks the "Account Settings" button.
     */
    @FXML
    private void openAccountSettings() {
        accountSettingsPopUp.setVisible(true);
    }

    /**
     * Opens the theme settings pop-up.
     * This method is called when the user clicks the "Theme Settings" button.
     */
    @FXML
    private void openThemeSettings() {themeSettingsPopUp.setVisible(true); }


    /**
     * Closes the specified pop-up.
     * This method is called when the user clicks the "Close" button in the pop-up.
     *
     * @param popUp The pop-up to be closed.
     */
    private void closePopUp(StackPane popUp) {
        popUp.setVisible(false);
    }

    /**
     * Closes the timer settings pop-up.
     * This method is called when the user clicks the "Close" button in the timer settings pop-up.
     */
    @FXML
    private void closeTimerSettings() {
        closePopUp(timerSettingsPopUp);
    }

    /**
     * Closes the account settings pop-up.
     * This method is called when the user clicks the "Close" button in the account settings pop-up.
     */
    @FXML
    private void closeAccountSettings() {
        closePopUp(accountSettingsPopUp);
    }

    /**
     * Closes the theme settings pop-up.
     * This method is called when the user clicks the "Close" button in the theme settings pop-up.
     */
    @FXML
    private void closeThemeSettings() { closePopUp(themeSettingsPopUp); }

    /**
     * Gets the root pane of the settings view.
     *
     * @return the root pane of the settings view.
     */
    @Override
    protected StackPane getRootPane() {
        return settings;
    }

    /**
     * Gets the content pane of the settings view.
     *
     * @return the content pane of the settings view.
     */
    @Override
    protected Region getContentPane() {
        return contentPane;
    }

    /**
     * Initializes the settings view.
     * This method is called when the controller is loaded.
     *
     * @throws SQLException if there is an error with the database connection.
     * @throws IOException  if there is an error with the FXML file.
     */
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
            colourSchemeSelector.setItems(FXCollections.observableArrayList(Theme.values()));
            timerUISelector.setItems(FXCollections.observableArrayList(Timers.values()));
        });

        pomodoroMinutesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,25) );
        pomodoroSecondsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0)  );
        shortBreakMinutesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,5)  );
        shortBreakSecondsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0)  );
        longBreakMinutesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,30) );
        longBreakSecondsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0)  );
        longBreakCyclesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,30,4)
        );

        System.out.println("SettingsController Initialization completed.");
    }

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

    @FXML
    private TextField newPasswordTextField;

    @FXML Button showNewPasswordButton;

    @FXML
    private TextField confirmNewPasswordTextField;

    @FXML
    private Button showConfirmNewPasswordButton;


    private boolean isSetPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    /**
     * Toggles the visibility of the password field in the account settings pop-up.
     * This method is called when the user clicks the "Show/Hide" button next to the password field.
     */
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

    /**
     * Toggles the visibility of the confirm password field in the account settings pop-up.
     * This method is called when the user clicks the "Show/Hide" button next to the confirm password field.
     */
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

    /**
     * Gets the password entered in the password field.
     * This method is used to retrieve the password when the user clicks the "Confirm" button.
     *
     * @return the password entered in the password field.
     */
    public String getSetPassword() {
        return isSetPasswordVisible ? newPasswordTextField.getText() : newPasswordEntryField.getText();
    }

    /**
     * Gets the password entered in the confirm password field.
     * This method is used to retrieve the password when the user clicks the "Confirm" button.
     *
     * @return the password entered in the confirm password field.
     */
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

    @FXML private Label settingsSavedLabel;

    /**
     * This method is called when the user clicks the "Save" button in the timer settings pop-up.
     * It saves the timer settings for the current user in the database.
     */
    @FXML
    private void saveTimerSettings() {

        // Check that the current user has timer settings
        timerDAO.createUserTimer(currentUser);

        // Get the current user's timer settings
        Timer currentUserTimer = timerDAO.getUserTimer(currentUser);

        // Get the input values from the spinners
        int pomodoroMinutesInput = Integer.parseInt(pomodoroMinutesSpinner.getValue().toString());
        int pomodoroSecondsInput = Integer.parseInt(pomodoroSecondsSpinner.getValue().toString());
        int shortBreakMinutesInput = Integer.parseInt(shortBreakMinutesSpinner.getValue().toString());
        int shortBreakSecondsInput = Integer.parseInt(shortBreakSecondsSpinner.getValue().toString());
        int longBreakMinutesInput = Integer.parseInt(longBreakMinutesSpinner.getValue().toString());
        int longBreakSecondsInput = Integer.parseInt(longBreakSecondsSpinner.getValue().toString());
        int longBreakCyclesInput = Integer.parseInt(longBreakCyclesSpinner.getValue().toString());

        // Set the timer settings for the current user
        currentUserTimer.setWorkDuration((pomodoroMinutesInput * 60) + pomodoroSecondsInput);
        currentUserTimer.setShortBreakDuration((shortBreakMinutesInput * 60) + shortBreakSecondsInput);
        currentUserTimer.setLongBreakDuration((longBreakMinutesInput * 60) + longBreakSecondsInput);
        currentUserTimer.setLongBreakAfter(longBreakCyclesInput);

        // Update the user's timer settings
        timerDAO.updateUserTimers(currentUser, currentUserTimer);
        settingsSavedLabel.setVisible(true);
    }

    /**
     * This method is called when the user clicks the "Save" button in the theme settings pop-up.
     * It saves the selected theme and timer UI settings for the current user.
     */
    @FXML
    private void saveThemeSettings() {
        if (colourSchemeSelector.getValue() != null) {
            ThemeManager.getInstance().applyTheme(getRootPane().getScene(), colourSchemeSelector.getValue());
            System.out.println("Theme: " + colourSchemeSelector.getValue() + " was applied");
        } else {
            System.out.println("Theme not selected");
        }
        if (timerUISelector.getValue() != null) {
            System.out.print("TimerUI Selection: " + timerUISelector.getValue() + ".");
        } else {
            System.out.println("TimerUI not selected");
        }

    }
}

