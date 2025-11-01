package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    /** ✅ Thêm 1 câu hỏi mới vào quiz */
    public static int insertQuestion(long quizId, Question q) {
        String sql = "INSERT INTO questions (quiz_id, question_text, correct_answer) VALUES (?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setLong(1, quizId);
            p.setString(2, q.getQuestionText());
            p.setString(3, q.getCorrectAnswer());
            p.executeUpdate();
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[QuestionDAO] insertQuestion error: " + e.getMessage());
        }
        return -1;
    }

    /** ✅ Lấy danh sách câu hỏi thuộc 1 quiz */
    public static List<Question> getQuestionsByQuiz(long quizId) {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE quiz_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, quizId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Question(
                            rs.getInt("question_id"),
                            rs.getString("question_text"),
                            null,
                            rs.getString("correct_answer")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[QuestionDAO] getQuestionsByQuiz error: " + e.getMessage());
        }
        return list;
    }

    /** ✅ Cập nhật câu hỏi */
    public static boolean updateQuestion(Question q) {
        String sql = "UPDATE questions SET question_text = ?, correct_answer = ? WHERE question_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, q.getQuestionText());
            p.setString(2, q.getCorrectAnswer());
            p.setInt(3, q.getId());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuestionDAO] updateQuestion error: " + e.getMessage());
        }
        return false;
    }

    /** ✅ Xóa câu hỏi (và options liên quan) */
    public static boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM questions WHERE question_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, questionId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[QuestionDAO] deleteQuestion error: " + e.getMessage());
        }
        return false;
    }
}
