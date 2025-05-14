package com.qut.cab302_project_pomodora.model;

import java.util.List;

/**
 * Interface for StudyPlan Data Access Object (DAO).
 * This interface defines the methods for interacting with the study plan data in the database.
 */
public interface IStudyPlanDAO {

    /**
     * Creates a new study plan in the database.
     * @param studyPlan The study plan to create.
     * @return true if the study plan was created successfully, false otherwise.
     */
    boolean createStudyPlan(StudyPlan studyPlan);

    /**
     * Retrieves all study plans from the database.
     * @return A list of all study plans.
     */
    List<StudyPlan> getAllStudyPlans(int currentUserId);

    /**
     * Retrieves a study plan from the database by its ID.
     * @param id The ID of the study plan to retrieve.
     * @return The study plan with the specified ID, or null if not found.
     */
    StudyPlan getStudyPlanById(int id);

    /**
     * Updates an existing study plan in the database.
     * @param studyPlan The study plan to update.
     * @return true if the study plan was updated successfully, false otherwise.
     */
    boolean updateStudyPlan(StudyPlan studyPlan);

    /**
     * Deletes a study plan from the database by its ID.
     * @param id The ID of the study plan to delete.
     * @return true if the study plan was deleted successfully, false otherwise.
     */
    boolean deleteStudyPlan(int id);

    /**
     * Retrieves study plans by their status.
     * @param userId The ID of the user.
     * @param status The status of the study plans to retrieve.
     * @return A list of study plans with the specified status.
     */
    List<StudyPlan> getStudyPlansByStatus(int userId, String status);

    /**
     * Retrieves a study plan from the database by its Title.
     * @param title The Title of the study plan to retrieve.
     * @return The study plan with the specified Title, or null if not found.
     */
    StudyPlan getStudyPlanByTitle(String title);
}
