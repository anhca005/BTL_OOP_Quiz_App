package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    /** ✅ Thêm bộ câu hỏi mới (quiz) */
    public static int insertQuiz(int teacherId, String title, String description) {
        String sql = "INSERT INTO quizzes (user_id, title, description) VALUES (?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, teacherId);
            p.setString(2, title);
            p.setString(3, description);
            p.executeUpdate();
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] insertQuiz error: " + e.getMessage());
        }
        return -1;
    }

    /** ✅ Lấy danh sách tất cả quiz theo giáo viên */
    public static List<Quiz> getAllByTeacher(int teacherId) {
        List<Quiz> list = new ArrayList<>();
        String sql = "SELECT * FROM quizzes WHERE user_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, teacherId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Quiz(
                            rs.getInt("quiz_id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getAllByTeacher error: " + e.getMessage());
        }
        return list;
    }

    /** ✅ Xóa 1 quiz (và các câu hỏi + options liên quan) */
    public static boolean deleteQuiz(int quizId) {
        String sql = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, quizId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] deleteQuiz error: " + e.getMessage());
        }
        return false;
    }

    public static Quiz getQuizByTeacherAndIndex(int teacherId, int quizNumber) {
        String sql = "SELECT * FROM quizzes WHERE user_id = ? ORDER BY quiz_id LIMIT 1 OFFSET ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, teacherId);
            p.setInt(2, quizNumber - 1); // OFFSET = sttQuiz - 1
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Quiz(
                            rs.getInt("quiz_id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
