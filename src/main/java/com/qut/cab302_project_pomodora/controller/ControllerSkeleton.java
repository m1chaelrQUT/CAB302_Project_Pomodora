// src/main/java/com/qut/cab302_project_pomodora/controller/ControllerSkeleton.java
package com.qut.cab302_project_pomodora.controller;

import com.qut.cab302_project_pomodora.Main;
import com.qut.cab302_project_pomodora.config.Theme;
import com.qut.cab302_project_pomodora.util.ThemeManager; // <-- Import ThemeManager
// Potentially import Theme enum if needed directly, though usually managed by ThemeManager
// import com.qut.cab302_project_pomodora.config.Theme;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract parent controller for shared logic such as proper scaling and theme application.
 * Concrete page controllers must provide the root pane and the content pane to be scaled.
 * Do so by implementing the getRootPane and getContentPane methods, returning the respective FXML object for the page
 */
public abstract class ControllerSkeleton {

    // Resolution of design - common property for scaling logic
    protected static final double DESIGN_WIDTH = 1920.0;
    protected static final double DESIGN_HEIGHT = 1080.0;

    // Listener fields - managed by the skeleton
    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    /**
     * Abstract method for subclasses to provide the main root pane (a StackPane).
     * This pane is used to get the Scene reference.
     * @return The root StackPane of the scene.
     */
    @FXML
    protected abstract StackPane getRootPane();

    /**
     * Abstract method for subclasses to provide the primary content container (Region).
     * This Region will be scaled to fit the scene according to DESIGN_WIDTH/HEIGHT.
     * @return The Region (e.g., VBox, AnchorPane) containing the main content.
     */
    @FXML
    protected abstract Region getContentPane();

    /**
     * Initializes the scaling listeners and applies the current theme.
     * The controllers should call this via super.initialize(), and override to implement page-specific intialisation.
     * See the home controller as example
     */
    @FXML
    public void initialize() {
        // Defer scene-dependent setup until the scene is guaranteed to be available.
        Platform.runLater(() -> {
            // Check if root pane itself is available first
            StackPane root = getRootPane();
            if (root == null) {
                System.err.println("Error in ControllerSkeleton initialize: getRootPane() returned null. Cannot setup theme or scaling.");
                return;
            }

            Scene scene = root.getScene();
            if (scene != null) {
                System.out.println("Scene obtained immediately in Platform.runLater. Applying theme and scaling.");
                applyThemeAndScaling(scene); // Call the combined setup method
            } else {
                // If the scene isn't ready yet, listen for it to become available
                root.sceneProperty().addListener((obs, oldScene, newScene) -> {
                    if (newScene != null) {
                        System.out.println("Scene obtained via sceneProperty listener. Applying theme and scaling.");
                        // Ensure setup runs on JavaFX thread
                        Platform.runLater(() -> applyThemeAndScaling(newScene)); // Call the combined setup method
                    } else if (oldScene != null) {
                        // Clean up scaling listeners if the scene is removed
                        removeScalingListeners(oldScene);
                    }
                });
                System.out.println("Scene not available immediately. Added listener to rootPane's sceneProperty.");
            }
        });
        System.out.println("ControllerSkeleton basic initialization completed (theme/scaling setup deferred).");

        // Call hook for subclasses AFTER scheduling the scene-dependent setup
        onInitialized();
    }

    /**
     * Hook method for subclasses to implement their specific initialization logic.
     * This is called after the base initialize() logic (scheduling theme/scaling) has run.
     */
    protected void onInitialized() {
        // Subclasses can override this
        System.out.println("Executing onInitialized() in " + this.getClass().getSimpleName());
    }


    /**
     * Applies the current theme and sets up scaling listeners once the Scene is available.
     * @param scene The Scene object.
     */
    protected void applyThemeAndScaling(Scene scene) {
        if (scene == null) {
            System.err.println("Attempted to apply theme and scaling with a null scene.");
            return;
        }
        System.out.println("Applying current theme to scene...");
        ThemeManager.getInstance().applyCurrentTheme(scene);

        System.out.println("Setting up scaling...");
        setupScaling(scene);
    }

    protected void changeTheme(Theme newTheme) {
        System.out.println("Changing theme to " + newTheme);
        Scene scene = getCurrentScene();
        ThemeManager.getInstance().applyTheme(scene, newTheme);
    }


    /**
     * Sets up the scaling listeners and performs the initial scale update.
     * @param scene The Scene object.
     */
    protected void setupScaling(Scene scene) {
        // Check scene again, although applyThemeAndScaling should have checked
        if (scene == null) {
            System.err.println("Attempted to setup scaling with a null scene (redundant check).");
            return;
        }

        addScalingListeners(scene);
        updateScale(scene); // Perform initial scaling
        System.out.println("Scaling setup complete for scene hash: " + scene.hashCode()); // Use hashcode for differentiation
    }


    private void addScalingListeners(Scene scene) {
        Region contentPane = getContentPane();
        if (contentPane == null) {
            System.err.println("Cannot add scaling listeners: ContentPane is null.");
            return;
        }

        // Create listeners if they don't exist
        if (widthListener == null) {
            widthListener = (obs, oldVal, newVal) -> Platform.runLater(() -> updateScale(scene));
        }
        if (heightListener == null) {
            heightListener = (obs, oldVal, newVal) -> Platform.runLater(() -> updateScale(scene));
        }

        // Ensure listeners are removed before adding (prevents duplicates if method is called multiple times)
        removeScalingListeners(scene);

        // Add new listeners
        scene.widthProperty().addListener(widthListener);
        scene.heightProperty().addListener(heightListener);
        System.out.println("Added scaling listeners to Scene hash: " + scene.hashCode());
    }

    private void removeScalingListeners(Scene scene) {
        if (scene == null) return;
        if (widthListener != null) {
            scene.widthProperty().removeListener(widthListener);
        }
        if (heightListener != null) {
            scene.heightProperty().removeListener(heightListener);
        }
        // Optional: Log removal
        // System.out.println("Removed scaling listeners from Scene hash: " + scene.hashCode());
    }

    private void updateScale(Scene scene) {
        Region contentPane = getContentPane();
        if (scene == null || contentPane == null) {
            // Reduce noise: This can happen legitimately during transitions
            // System.err.println("Cannot update scale: Scene or ContentPane is null.");
            return;
        }

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        if (sceneWidth <= 1 || sceneHeight <= 1 || DESIGN_WIDTH <= 0 || DESIGN_HEIGHT <= 0) {
            // Reset scale if dimensions are invalid/too small
            if (contentPane.getScaleX() != 1.0 || contentPane.getScaleY() != 1.0) {
                contentPane.setScaleX(1.0);
                contentPane.setScaleY(1.0);
            }
            return;
        }

        double scaleX = sceneWidth / DESIGN_WIDTH;
        double scaleY = sceneHeight / DESIGN_HEIGHT;

        contentPane.setScaleX(scaleX);
        contentPane.setScaleY(scaleY);
    }

    /**
     * Utility method to get the current scene associated with this controller's root pane.
     * Useful for operations that need the scene after initialization (e.g., theme switching UI).
     * @return The current Scene, or null if the root pane is null or not attached to a scene.
     */
    protected Scene getCurrentScene() {
        StackPane root = getRootPane(); // Use the getter method
        return (root != null) ? root.getScene() : null;
    }

    protected void navigateTo(String toSceneName) throws IOException {
        Scene currentScene = getCurrentScene();
        Stage stage = (Stage) currentScene.getWindow();
        String toScenePath = "/com/qut/cab302_project_pomodora/fxml/" + toSceneName + ".fxml";
        System.out.println("Navigating to: " + toScenePath);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(toScenePath));
        Scene scene = new Scene(fxmlLoader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(scene);
    }
}