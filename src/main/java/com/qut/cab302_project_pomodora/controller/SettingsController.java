package com.qut.cab302_project_pomodora.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

    @FXML private Region navbar;
    @FXML private NavbarController navbarController;


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

        contentPane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        contentPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        contentPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Platform.runLater(() -> {
            navbarController.setNavButtonStyles(settings.getScene());
        });

        System.out.println("SettingsController Initialization completed.");
    }

    /* Account Settings Pop Up Methods*/
    @FXML
    private PasswordField newPasswordEntryField;

    @FXML
    private TextField newPasswordTextField;

    @FXML Button showNewPasswordButton;


    @FXML
    private PasswordField confirmNewPasswordEntryField;

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

    @FXML
    private void confirmPasswordChange() {
        System.out.println("Confirm password change.");
    }

}
