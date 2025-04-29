package com.qut.cab302_project_pomodora.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyPlanDAO {

    private static final String SELECT_ALL = "SELECT * FROM study_plans";
    private static final String SELECT_BY_ID = "SELECT * FROM study_plans WHERE id = ?";
    private static final String SELECT_BY_USER = "SELECT * FROM study_plans WHERE user_id = ?";
    private static final String SELECT_BY_STATUS = "SELECT * FROM study_plans WHERE user_id = ? AND status = ?";
    private static final String INSERT = "INSERT INTO study_plans(user_id, title, description, status, participant_count) VALUES(?,?,?,?,?)";
    private static final String UPDATE = "UPDATE study_plans SET title = ?, description = ?, status = ?, participant_count = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    private static final String DELETE = "DELETE FROM study_plans WHERE id = ?";


    public List<StudyPlan> getAllStudyPlans() {
        List<StudyPlan> studyPlans = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                studyPlans.add(mapResultSetToStudyPlan(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching all study plans", e);
        }
        return studyPlans;
    }

    public StudyPlan getStudyPlanById(int id) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudyPlan(rs);
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching study plan by ID: " + id, e);
        }
        return null;
    }

    public List<StudyPlan> getStudyPlansByUser(int userId) {
        List<StudyPlan> studyPlans = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_USER)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                studyPlans.add(mapResultSetToStudyPlan(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching study plans by user: " + userId, e);
        }
        return studyPlans;
    }

    public List<StudyPlan> getStudyPlansByStatus(int userId, String status) {
        List<StudyPlan> studyPlans = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_STATUS)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                studyPlans.add(mapResultSetToStudyPlan(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching study plans by status: " + status, e);
        }
        return studyPlans;
    }

    public boolean addStudyPlan(StudyPlan studyPlan) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, studyPlan.getUserId());
            pstmt.setString(2, studyPlan.getTitle());
            pstmt.setString(3, studyPlan.getDescription());
            pstmt.setString(4, studyPlan.getStatus());
            pstmt.setInt(5, studyPlan.getParticipantCount());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        studyPlan.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Error adding study plan", e);
        }
        return false;
    }

    public boolean updateStudyPlan(StudyPlan studyPlan) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {

            pstmt.setString(1, studyPlan.getTitle());
            pstmt.setString(2, studyPlan.getDescription());
            pstmt.setString(3, studyPlan.getStatus());
            pstmt.setInt(4, studyPlan.getParticipantCount());
            pstmt.setInt(5, studyPlan.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Error updating study plan: " + studyPlan.getId(), e);
            return false;
        }
    }

    public boolean deleteStudyPlan(int id) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Error deleting study plan: " + id, e);
            return false;
        }
    }

    // Helper Methods
    private StudyPlan mapResultSetToStudyPlan(ResultSet rs) throws SQLException {
        StudyPlan plan = new StudyPlan();
        plan.setId(rs.getInt("id"));
        plan.setUserId(rs.getInt("user_id"));
        plan.setTitle(rs.getString("title"));
        plan.setDescription(rs.getString("description"));
        plan.setStatus(rs.getString("status"));
        plan.setParticipantCount(rs.getInt("participant_count"));
        return plan;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}