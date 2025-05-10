package com.qut.cab302_project_pomodora.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SqliteStudyPlanDAO {
    // Database connection
    private Connection connection;

    // Current user
    User currentUser = SessionManager.getCurrentUser();

    /**
     * Constructor for SqliteStudyPlanDAO
     * Initializes the connection to the database and creates the study plans table if it doesn't exist.
     */
    public SqliteStudyPlanDAO() {
        connection = SqliteConnection.getInstance();
        // If table doesn't exist
        createTable();
    }

    /**
     * Creates the study plans table if it doesn't exist.
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS studyPlans ("
                    + "Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userId INTEGER NOT NULL,"
                    + "title VARCHAR NOT NULL,"
                    + "description VARCHAR NOT NULL,"
                    + "status VARCHAR NOT NULL,"
                    + "participantCount INTEGER NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: Replace with a more robust logging system
        }
    }


    // SQL Queries
    private static final String SELECT_ALL = "SELECT * FROM study_plans where userId = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM study_plans WHERE userId = ? AND id = ? ";
    private static final String SELECT_BY_STATUS = "SELECT * FROM study_plans WHERE user_id = ? AND status = ?";
    private static final String INSERT = "INSERT INTO study_plans(user_id, title, description, status, participant_count) VALUES(?,?,?,?,?)";
    private static final String UPDATE = "UPDATE study_plans SET title = ?, description = ?, status = ?, participant_count = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    private static final String DELETE = "DELETE FROM study_plans WHERE id = ?";


    /**
     * Retrieves all study plans for the current user.
     * @return List of StudyPlan objects
     */
    public List<StudyPlan> getAllStudyPlans() {
        List<StudyPlan> studyPlans = new ArrayList<>();
        try{
            // Prepare the SQL statement
            PreparedStatement statement= connection.prepareStatement(SELECT_ALL);
            // Set the userId parameter
            statement.setInt(1, currentUser.getId());
            ResultSet resultSet = statement.executeQuery();

            // Loop through the result set and add each study plan to the list
            while(resultSet.next()) {
                studyPlans.add(mapResultSetToStudyPlan(resultSet));
            }
            // Return the list of study plans
            return studyPlans;
        } catch (SQLException e) {
            handleSQLException("Error retrieving study plan by id: " + currentUser.getId(), e); //TODO: add the user id (save to local var)
        }
        return null;
    }


    /**
     * Retrieves a study plan by its ID.
     * @param id The ID of the study plan to retrieve.
     * @return The StudyPlan object if found, null otherwise.
     */
    public StudyPlan getStudyPlanById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID);
            preparedStatement.setInt(1, currentUser.getId());
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToStudyPlan(resultSet);
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving study plan by id: " + id, e);
        }
        return null;
    }

    /**
     * Retrieves study plans by their status.
     * @param userId The ID of the user.
     * @param status The status of the study plans to retrieve.
     * @return List of StudyPlan objects with the specified status.
     */
    public List<StudyPlan> getStudyPlansByStatus(int userId, String status) {
        List<StudyPlan> studyPlans = new ArrayList<>();

        try (
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_STATUS)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studyPlans.add(mapResultSetToStudyPlan(resultSet));
            }
            return studyPlans;
        } catch (SQLException e) {
            handleSQLException("Error retrieving study plan by Status: " + status, e);
        }
        return null;
    }

    /**
     * Adds a new study plan to the database.
     * @param studyPlan The StudyPlan object to add.
     * @return true if the study plan was added successfully, false otherwise.
     */
    public boolean addStudyPlan(StudyPlan studyPlan) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, studyPlan.getUserId());
            preparedStatement.setString(2, studyPlan.getTitle());
            preparedStatement.setString(3, studyPlan.getDescription());
            preparedStatement.setString(4, studyPlan.getStatus());
            preparedStatement.setInt(5, studyPlan.getParticipantCount());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        studyPlan.setId(resultSet.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error adding study plan", e);
        }
        return false;
    }

    /**
     * Updates an existing study plan in the database.
     * @param studyPlan The StudyPlan object with updated information.
     * @return true if the study plan was updated successfully, false otherwise.
     */
    public boolean updateStudyPlan(StudyPlan studyPlan) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);

            preparedStatement.setString(1, studyPlan.getTitle());
            preparedStatement.setString(2, studyPlan.getDescription());
            preparedStatement.setString(3, studyPlan.getStatus());
            preparedStatement.setInt(4, studyPlan.getParticipantCount());
            preparedStatement.setInt(5, studyPlan.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Study plan updated successfully: " + studyPlan.getId());
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error updating study plan: " + studyPlan.getId(), e);
        }
        return false;
    }

    /**
     * Deletes a study plan from the database.
     * @param id The ID of the study plan to delete.
     * @return true if the study plan was deleted successfully, false otherwise.
     */
    public boolean deleteStudyPlan(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Study plan: " + id + " deleted successfully.");
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error deleting study plan: " + id, e);
        }
        return false;
    }

    /**
     * Maps a ResultSet to a StudyPlan object.
     * @param resultSet The ResultSet to map.
     * @return The StudyPlan object.
     * @throws SQLException If an SQL error occurs.
     */
    private StudyPlan mapResultSetToStudyPlan(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("userId");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String status = resultSet.getString("status");
        int participantCount = resultSet.getInt("participantCount");
        StudyPlan studyPlan = new StudyPlan(userId, title, description, status);
        return studyPlan;
    }

    /**
     * Handles SQL exceptions by printing the error message and stack trace.
     * @param message The error message.
     * @param e The SQLException object.
     */
    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}