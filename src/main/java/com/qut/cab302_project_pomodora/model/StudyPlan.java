package com.qut.cab302_project_pomodora.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a study plan in the application.
 * A study plan contains information about the user, title, description, status, tasks, and participant count.
 */
public class StudyPlan
{
    private int id;
    private int userId;
    private String title;
    private String description;
    private String status;
    private List<StudyTask> studyTasks;

    /**
     * Default constructor for StudyPlan.
     * Initializes a new instance of the StudyPlan class.
     */
    public  StudyPlan()
    {
        this.studyTasks = new ArrayList<>();
    }

    /**
     * Constructor for StudyPlan.
     * Initializes a new instance of the StudyPlan class with the specified parameters.
     *
     * @param userId      The ID of the user associated with the study plan.
     * @param title       The title of the study plan.
     * @param description The description of the study plan.
     * @param status      The status of the study plan (e.g., ACTIVE, INACTIVE).
     */
    public StudyPlan(int id, int userId,String title,String description,String status)
    {
        this();
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
    }


    /**
     * Gets the ID of the study plan.
     * @return The ID of the study plan.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the study plan.
     * @param id The ID to set for the study plan.
     */
    public void setId(int id) {
        this.id = id; //hard code this shit
    }

    /**
     * Gets the user ID associated with the study plan.
     * @return The user ID of the study plan.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the study plan.
     * @param userId The user ID to set for the study plan.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the title of the study plan.
     * @return The title of the study plan.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the title of the study plan.
     * @param title The title to set for the study plan.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the status of the study plan.
     * @return The status of the study plan.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the study plan.
     * @param status The status to set for the study plan.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the description of the study plan.
     * @return The description of the study plan.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the study plan.
     * @param description The description to set for the study plan.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of tasks associated with the study plan.
     * @return The list of tasks in the study plan.
     */
    public List<StudyTask> tasks() {
        if (studyTasks == null || studyTasks.size() == 0) {
            return null;
        }
        return studyTasks;
    }

    /**
     * Sets the list of tasks associated with the study plan.
     * @param studyTasks The list of tasks to set for the study plan.
     */
    public void setTasks(List<StudyTask> studyTasks) {
        this.studyTasks = studyTasks;
    }


    /**
     * Returns a string representation of the study plan.
     * @return A string representation of the study plan.
     */
    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * Checks if the study plan is active.
     * @return true if the study plan is active, false otherwise.
     */
    public boolean planIsActive() {
        return status.equals("ACTIVE");
    }
}