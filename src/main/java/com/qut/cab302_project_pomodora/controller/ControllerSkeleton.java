package com.qut.cab302_project_pomodora.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Abstract parent controller for shared logic such as proper scaling.
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
    @FXML // Keep FXML annotation. It may be useful to be able to call from fxml id.
    protected abstract StackPane getRootPane();

    /**
     * Abstract method for subclasses to provide the primary content container (Region).
     * This Region will be scaled to fit the scene according to DESIGN_WIDTH/HEIGHT.
     * @return The Region (e.g., VBox, AnchorPane) containing the main content.
     * All standard layout containers are children of Region
     */
    @FXML // Keep FXML annotation. It may be useful to be able to call from fxml id.
    protected abstract Region getContentPane();

    /**
     * Initializes the scaling listeners.
     * The controllers should call this via super.initialize(), and override to implement page-specific intialisation.
     * See the home controller as example
     */
    @FXML
    public void initialize() {
        // Defer scaling setup until the scene is guaranteed to be available.
        // Use Platform.runLater to handle potential delays in scene attachment.
        Platform.runLater(() -> {
            Scene scene = getRootPane().getScene(); // Use the getter method
            if (scene != null) {
                System.out.println("Scene obtained immediately in Platform.runLater. Applying scaling.");
                setupScaling(scene);
            } else {
                // If the scene isn't ready yet, listen for it to become available
                getRootPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
                    if (newScene != null) {
                        System.out.println("Scene obtained via sceneProperty listener. Applying scaling.");
                        // Ensure setup runs on JavaFX thread if listener triggers off-thread (unlikely here, but safe)
                        Platform.runLater(() -> setupScaling(newScene));
                    } else if (oldScene != null) {
                        // Clean up listeners if the scene is removed
                        removeScalingListeners(oldScene);
                    }
                });
                System.out.println("Scene not available immediately. Added listener to rootPane's sceneProperty.");
            }
        });
        System.out.println("ControllerSkeleton basic initialization completed (scaling setup deferred).");
    }


    /**
     * Sets up the scaling listeners and performs the initial scale update.
     * This should be called once the Scene is available.
     * @param scene The Scene object.
     */
    protected void setupScaling(Scene scene) {
        if (scene == null) {
            System.err.println("Attempted to setup scaling with a null scene.");
            return;
        }
        addScalingListeners(scene);
        updateScale(scene); // Perform initial scaling
        System.out.println("Scaling setup complete for scene: " + scene);
    }


    private void addScalingListeners(Scene scene) {
        // Ensure contentPane was found
        if (getContentPane() == null) {
            System.err.println("Cannot add scaling listeners: ContentPane is null.");
            return;
        }

        // Create listeners
        if (widthListener == null) {
            widthListener = (obs, oldVal, newVal) -> Platform.runLater(() -> updateScale(scene));
        }
        if (heightListener == null) {
            heightListener = (obs, oldVal, newVal) -> Platform.runLater(() -> updateScale(scene));
        }

        // Ensure no listeners
        removeScalingListeners(scene);

        // Add new listeners
        scene.widthProperty().addListener(widthListener);
        scene.heightProperty().addListener(heightListener);
        System.out.println("Added scaling listeners to Scene.");
    }

    private void removeScalingListeners(Scene scene) {
        if (scene == null) return;
        if (widthListener != null) {
            scene.widthProperty().removeListener(widthListener);
        }
        if (heightListener != null) {
            scene.heightProperty().removeListener(heightListener);
        }
    }

    private void updateScale(Scene scene) {
        Region contentPane = getContentPane(); // Use getter
        if (scene == null || contentPane == null) {
            System.err.println("Cannot update scale: Scene or ContentPane is null.");
            return;
        }

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        // Prevent scaling issues with invalid dimensions
        if (sceneWidth <= 1 || sceneHeight <= 1 || DESIGN_WIDTH <= 0 || DESIGN_HEIGHT <= 0) {
            // System.out.println("Scene/Design dimensions invalid for scaling, resetting scale to 1.0.");
            contentPane.setScaleX(1.0);
            contentPane.setScaleY(1.0);
            return;
        }

        double scaleX = sceneWidth / DESIGN_WIDTH;
        double scaleY = sceneHeight / DESIGN_HEIGHT;

        contentPane.setScaleX(scaleX);
        contentPane.setScaleY(scaleY);

    }
}