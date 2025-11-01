package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class QuizDAO {

    private static final Random random = new Random();

    /**
     * Tạo một ID quiz ngẫu nhiên gồm 11 chữ số.
     * Đảm bảo ID là duy nhất bằng cách kiểm tra trong DB.
     */
    public static long generateRandomQuizId() {
        long id;
        do {
            // Tạo số ngẫu nhiên từ 10^10 đến 10^11 - 1
            id = 10_000_000_000L + (long)(random.nextDouble() * (99_999_999_999L - 10_000_000_000L + 1));
        } while (isQuizIdExists(id)); // Kiểm tra xem ID đã tồn tại chưa
        return id;
    }

    /**
     * Kiểm tra xem một quiz ID đã tồn tại trong cơ sở dữ liệu chưa.
     */
    private static boolean isQuizIdExists(long quizId) {
        String sql = "SELECT COUNT(*) FROM quizzes WHERE quiz_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, quizId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] isQuizIdExists error: " + e.getMessage());
        }
        return false;
    }

    /** Thêm bộ câu hỏi mới (quiz) */
    public static long insertQuiz(long quizId, int teacherId, String title, String description, int duration) {
        String sql = "INSERT INTO quizzes (quiz_id, user_id, title, description, duration) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, quizId);
            p.setInt(2, teacherId);
            p.setString(3, title);
            p.setString(4, description);
            p.setInt(5, duration);
            p.executeUpdate();
            return quizId;
        } catch (SQLException e) {
            System.err.println("[QuizDAO] insertQuiz error: " + e.getMessage());
        }
        return -1L;
    }

    public static List<Quiz> getAllByTeacher(int teacherId) {
        List<Quiz> list = new ArrayList<>();
        String sql = "SELECT * FROM quizzes WHERE user_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, teacherId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Quiz(
                            rs.getLong("quiz_id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("duration"),
                            rs.getString("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuizDAO] getAllByTeacher error: " + e.getMessage());
        }
        return list;
    }

    /** Xóa 1 quiz (và các câu hỏi + options liên quan) */
    public static boolean deleteQuiz(long quizId) {
        String sql = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, quizId);
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
                            rs.getLong("quiz_id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("duration"),
                            rs.getString("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Quiz getQuizById(long quizId) {
        String sql = "SELECT * FROM quizzes WHERE quiz_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, quizId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Quiz(
                            rs.getLong("quiz_id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("duration"),
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
