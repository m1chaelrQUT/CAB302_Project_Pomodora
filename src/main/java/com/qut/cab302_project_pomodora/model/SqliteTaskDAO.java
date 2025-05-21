package com.qut.cab302_project_pomodora.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SqliteTaskDAO is a Data Access Object (DAO) for managing tasks in a SQLite database.
 * It provides methods to create, read, update, and delete tasks.
 */
public class SqliteTaskDAO implements ITaskDAO {
    // Database connection
    private Connection connection;

    // Current user
    User currentUser = SessionManager.getCurrentUser();

    /**
     * Constructor for SqliteStudyPlanDAO
     * Initializes the connection to the database and creates the study plans table if it doesn't exist.
     */
    public SqliteTaskDAO() {
        connection = SqliteConnection.getInstance();
        // If table doesn't exist
        createTable();
    }

    /**
     * Creates the tasks table if it doesn't exist.
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS tasks ("
                    + "Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "studyPlanId INTEGER NOT NULL,"
                    + "taskNumber INTEGER NOT NULL,"
                    + "title VARCHAR NOT NULL,"
                    + "description VARCHAR NOT NULL,"
                    + "status VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: Replace with a more robust logging system
        }
    }

    // SQL Queries
    private static final String SELECT_BY_STUDY_PLAN = "SELECT * FROM tasks WHERE studyPlanId = ?";
    private static final String INSERT = "INSERT INTO tasks(studyPlanId, taskNumber, title, description, status) VALUES(?,?,?,?,?)";
    private static final String UPDATE = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM tasks WHERE id = ?";
    private static final String SELECT_STATUS_BY_TASK = "SELECT status FROM tasks WHERE id = ?";

    /**
     * Retrieves all tasks for a given study plan.
     * @param studyPlanId The ID of the study plan.
     * @return List of Task objects associated with the study plan.
     */
    public List<StudyTask> getTasksByStudyPlan(int studyPlanId) {
        List<StudyTask> studyTasks = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_STUDY_PLAN);

            preparedStatement.setInt(1, studyPlanId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No tasks found for study plan ID: " + studyPlanId);
                return studyTasks; // Return an empty list if no tasks are found
            } else {
                while (resultSet.next()) {
                    studyTasks.add(mapResultSetToTask(resultSet));
                }
                System.out.println("Tasks fetched successfully for study plan ID: " + studyPlanId);
                return studyTasks;
            }

        } catch (SQLException e) {
            handleSQLException("Error fetching tasks by study plan: " + studyPlanId, e);
        }
        return null;
        // TODO: Handle the case where no tasks are found
    }

    /**
     * Retieves the current study plan's number of remaining tasks.
     * @param studyPlanId The ID of the study plan.
     * @return The number of remaining tasks in the study plan.
     */
    public int tasksRemaining(int studyPlanId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM tasks WHERE studyPlanId = ? AND status = 'INCOMPLETE'");
            preparedStatement.setInt(1, studyPlanId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Remaining tasks: " + resultSet.getInt(1) + " for study plan ID: " + studyPlanId);
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving remaining tasks for study plan: " + studyPlanId, e);
        }
        return 0;
    }

    /**
     * Creates a new task in the database.
     * @param studyTask The Task object to be created.
     * @return true if the task was created successfully, false otherwise.
     */
    public boolean createTask(StudyTask studyTask) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, studyTask.getStudyPlanId());
            preparedStatement.setInt(2, studyTask.getTaskNumber());
            preparedStatement.setString(3, studyTask.getTitle());
            preparedStatement.setString(4, studyTask.getDescription());
            preparedStatement.setString(5, studyTask.getStatus());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        studyTask.setId(resultSet.getInt(1));
                    } //TODO: check this works
                }
                System.out.println("Task created successfully with ID: " + studyTask.getId());
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error adding task", e);
        }
        return false;
    }

    /**
     * Updates an existing task in the database.
     * @param studyTask The Task object with updated information.
     * @return true if the task was updated successfully, false otherwise.
     */
    @Override
    public boolean updateTask(StudyTask studyTask) {
        try {
            System.out.println("Updating task: " + studyTask.getId());
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, studyTask.getTitle());
            preparedStatement.setString(2, studyTask.getDescription());
            preparedStatement.setString(3, studyTask.getStatus());
            preparedStatement.setInt(4, studyTask.getId());

            int affectedRows = preparedStatement.executeUpdate();
            // Check if the update was successful
            if (affectedRows == 0) {
                System.out.println("No task found with ID: " + studyTask.getId());
                return false;
            }
            if (affectedRows > 0) {
                System.out.println("Task updated successfully: " + studyTask.getId());
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error updating task: " + studyTask.getId(), e);
        }
        return false;
    }

    /**
     * Deletes a task from the database.
     * @param id The ID of the task to delete.
     * @return true if the task was deleted successfully, false otherwise.
     */
    public boolean deleteTask(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task deleted successfully: " + id);
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error deleting task: " + id, e);
        }
        return false;
    }

    /**
     * Maps a ResultSet to a Task object.
     *
     * @param resultSet The ResultSet containing task data.
     * @return A Task object populated with data from the ResultSet.
     * @throws SQLException If an SQL error occurs while processing the ResultSet.
     */
    private StudyTask mapResultSetToTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int studyPlanId = resultSet.getInt("studyPlanId");
        int taskNumber = resultSet.getInt("taskNumber");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String status = resultSet.getString("status");
        StudyTask studyTask = new StudyTask(studyPlanId, taskNumber, title, description, status);
        return studyTask;
    }

    public boolean isTaskComplete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STATUS_BY_TASK);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("status");
                return "COMPLETE".equalsIgnoreCase(status);
            }
        } catch (SQLException e) {
            handleSQLException("Error checking task status for task ID: " + id, e);
        }
        return false;
    }

    /**
     * Handles SQL exceptions by printing the error message and stack trace.
     * @param message The custom error message to display.
     * @param e The SQLException that occurred.
     */
    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}