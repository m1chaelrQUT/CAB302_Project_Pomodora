package com.qut.cab302_project_pomodora.model;

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
}
