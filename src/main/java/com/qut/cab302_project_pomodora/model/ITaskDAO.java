package com.qut.cab302_project_pomodora.model;

import java.util.List;

public interface ITaskDAO {
    /**
     * Creates a new task in the database.
     * @param studyTask The task to create.
     * @return true if the task was created successfully, false otherwise.
     */
    boolean createTask(StudyTask studyTask);

    /**
     * Retrieves the remaining number of tasks in a study plan.
     * @param studyPlanId The ID of the study plan.
     * @return The number of remaining tasks in the study plan.
     */
    int tasksRemaining(int studyPlanId);

    /**
     * Retrieves all tasks associated with a specific study plan from the database.
     * @param studyPlanId The ID of the study plan.
     * @return A list of tasks associated with the specified study plan.
     */
    List<StudyTask> getTasksByStudyPlan(int studyPlanId);

    /**
     * Updates an existing task in the database.
     * @param studyTask The task to update.
     * @return true if the task was updated successfully, false otherwise.
     */
    boolean updateTask(StudyTask studyTask);

    /**
     * Deletes a task from the database by its ID.
     * @param id The ID of the task to delete.
     * @return true if the task was deleted successfully, false otherwise.
     */
    boolean deleteTask(int id);
}
