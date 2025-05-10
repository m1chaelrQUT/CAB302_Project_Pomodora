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
    private static final String SELECT_BY_STUDY_PLAN = "SELECT * FROM tasks WHERE study_plan_id = ?";
    private static final String INSERT = "INSERT INTO tasks(studyPlanId, taskNumber, title, description, status) VALUES(?,?,?,?,?)";
    private static final String UPDATE = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM tasks WHERE id = ?";

    public List<Task> getTasksByStudyPlan(int studyPlanId) {
        List<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_STUDY_PLAN);

            preparedStatement.setInt(1, studyPlanId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tasks.add(mapResultSetToTask(resultSet));
            }
            System.out.println("Tasks fetched successfully for study plan ID: " + studyPlanId);
            return tasks;
        } catch (SQLException e) {
            handleSQLException("Error fetching tasks by study plan: " + studyPlanId, e);
        }
        return null;
    }

    public boolean createTask(Task task) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, task.getStudyPlanId());
            preparedStatement.setInt(2, task.getTaskNumber());
            preparedStatement.setString(3, task.getTitle());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setString(5, task.getStatus());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        task.setId(resultSet.getInt(1));
                    } //TODO: check this works
                }
                System.out.println("Task created successfully with ID: " + task.getId());
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error adding task", e);
        }
        return false;
    }

    public boolean updateTask(Task task) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus());
            preparedStatement.setInt(4, task.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task updated successfully: " + task.getId());
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error updating task: " + task.getId(), e);
        }
        return false;
    }

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

    // Helper Methods
    private Task mapResultSetToTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int studyPlanId = resultSet.getInt("studyPlanId");
        int taskNumber = resultSet.getInt("taskNumber");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String status = resultSet.getString("status");
        Task task = new Task(studyPlanId, taskNumber, title, description, status);
        return task;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}