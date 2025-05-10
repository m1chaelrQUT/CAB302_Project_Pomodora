package com.qut.cab302_project_pomodora.model;

import java.util.List;

public interface ITaskDAO {
    /**
     * Creates a new task in the database.
     * @param task The task to create.
     * @return true if the task was created successfully, false otherwise.
     */
    boolean createTask(Task task);

    /**
     * Retrieves all tasks associated with a specific study plan from the database.
     * @param studyPlanId The ID of the study plan.
     * @return A list of tasks associated with the specified study plan.
     */
    List<Task> getTasksByStudyPlan(int studyPlanId);

    /**
     * Updates an existing task in the database.
     * @param task The task to update.
     * @return true if the task was updated successfully, false otherwise.
     */
    boolean updateTask(Task task);

    /**
     * Deletes a task from the database by its ID.
     * @param id The ID of the task to delete.
     * @return true if the task was deleted successfully, false otherwise.
     */
    boolean deleteTask(int id);
}
