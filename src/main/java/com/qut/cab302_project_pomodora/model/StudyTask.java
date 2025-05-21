package com.qut.cab302_project_pomodora.model;

/**
 * Represents a task in the study plan.
 * A task contains information about the study plan ID, task number, title, description, and status.
 */
public class StudyTask {
    private int id;
    private int studyPlanId;
    private int taskNumber;
    public String title;
    private String description;
    private String status;

    /**
     * Default constructor for Task.
     * Initializes a new instance of the Task class.
     */
    public StudyTask(int studyPlanId, int taskNumber, String title, String description, String status) {
        this.studyPlanId = studyPlanId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    /**
     * Get the ID of the task.
     * @return The ID of the task.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of the task.
     * @param id The ID to set for the task.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the study plan ID associated with the task.
     * @return The study plan ID.
     */
    public int getStudyPlanId() {
        return studyPlanId;
    }

    /**
     * Get the task number.
     * @return The task number.
     */
    public int getTaskNumber() {
        return taskNumber;
    }

    /**
     * Set the task number.
     * @param taskNumber The task number to set.
     */
    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Get the title of the task.
     * @return The title of the task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the task.
     * @param title The title to set for the task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the description of the task.
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the task.
     * @param description The description to set for the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the status of the task.
     * @return The status of the task.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the task.
     * @param status The status to set for the task.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the string representation of the task.
     * @return A string representation of the task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", studyPlanId=" + studyPlanId +
                ", title='" + title + '\'' +
                ", status='" + status +
                '}';
    }
}




