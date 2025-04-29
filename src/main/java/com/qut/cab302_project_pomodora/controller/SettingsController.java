package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.model.IUserDAO;
import com.qut.cab302_project_pomodora.model.SqliteUserDAO;
import com.qut.cab302_project_pomodora.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class SettingsController extends ControllerSkeleton {
    @FXML
    private StackPane settings;
    @FXML
    private StackPane contentPane;
    @FXML
    private StackPane timerSettingsPopUp;
    @FXML
    private StackPane accountSettingsPopUp;

    // Input fields for Account Settings
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField newPasswordEntryField;
    @FXML
    private PasswordField confirmNewPasswordEntryField;

    // Mock user for testing TODO: Remove this and load the user from the session
    private User currentUser = new User("MicRob", "Michael", 1, 0, "MichaelEmail");

    // User DAO interface
    private IUserDAO userDAO;

    public SettingsController() {
        userDAO = new SqliteUserDAO();
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
    public void initialize() {
        super.initialize();
        //TODO: Load the Session and get the user

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        System.out.println("SettingsController Initialization completed.");
    }

    // TODO: Add the iniSession() method to load the session and get the user

    /* Account Settings Pop Up Methods*/

    @FXML
    private TextField newPasswordTextField;

    @FXML Button showNewPasswordButton;

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
    }

}
