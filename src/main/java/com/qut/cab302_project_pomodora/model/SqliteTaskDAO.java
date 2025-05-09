package com.qut.cab302_project_pomodora.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteTaskDAO {
    // SQL Queries
    private static final String SELECT_ALL = "SELECT * FROM tasks";
    private static final String SELECT_BY_ID = "SELECT * FROM tasks WHERE id = ?";
    private static final String SELECT_BY_STUDY_PLAN = "SELECT * FROM tasks WHERE study_plan_id = ?";
    private static final String SELECT_BY_USER = "SELECT * FROM tasks WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO tasks(study_plan_id, user_id, title, description, status, is_sticky) VALUES(?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE tasks SET study_plan_id = ?, title = ?, description = ?, status = ?, is_sticky = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    private static final String DELETE = "DELETE FROM tasks WHERE id = ?";

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching all tasks", e);
        }
        return tasks;
    }

    public Task getTaskById(int id) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTask(rs);
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching task by ID: " + id, e);
        }
        return null;
    }

    public List<Task> getTasksByStudyPlan(int studyPlanId) {
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_STUDY_PLAN)) {

            pstmt.setInt(1, studyPlanId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching tasks by study plan: " + studyPlanId, e);
        }
        return tasks;
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_USER)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching tasks by user: " + userId, e);
        }
        return tasks;
    }

    public boolean addTask(Task task) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, task.getStudyPlanId());
            pstmt.setInt(2, task.getUserId());
            pstmt.setString(3, task.getTitle());
            pstmt.setString(4, task.getDescription());
            pstmt.setString(5, task.getStatus());
            pstmt.setBoolean(6, task.isSticky());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        task.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error adding task", e);
        }
        return false;
    }

    public boolean updateTask(Task task) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {

            pstmt.setInt(1, task.getStudyPlanId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getStatus());
            pstmt.setBoolean(5, task.isSticky());
            pstmt.setInt(6, task.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Error updating task: " + task.getId(), e);
            return false;
        }
    }

    public boolean deleteTask(int id) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Error deleting task: " + id, e);
            return false;
        }
    }

    // Helper Methods
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setStudyPlanId(rs.getInt("study_plan_id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setSticky(rs.getBoolean("is_sticky"));

        return task;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}