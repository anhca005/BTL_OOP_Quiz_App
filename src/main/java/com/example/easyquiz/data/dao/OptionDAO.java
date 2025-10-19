package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OptionDAO {

    /** ✅ Thêm các lựa chọn cho 1 câu hỏi */
    public static void insertOptions(int questionId, String[] options) {
        if (options == null) return;
        String sql = "INSERT INTO options (question_id, text, display_order) VALUES (?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            for (int i = 0; i < options.length; i++) {
                if (options[i] == null || options[i].isEmpty()) continue;
                p.setInt(1, questionId);
                p.setString(2, options[i]);
                p.setInt(3, i + 1);
                p.addBatch();
            }
            p.executeBatch();
        } catch (SQLException e) {
            System.err.println("[OptionDAO] insertOptions error: " + e.getMessage());
        }
    }

    /** ✅ Lấy danh sách lựa chọn theo question_id */
    public static List<String> getOptionsByQuestion(int questionId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT text FROM options WHERE question_id = ? ORDER BY display_order";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, questionId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) list.add(rs.getString("text"));
            }
        } catch (SQLException e) {
            System.err.println("[OptionDAO] getOptionsByQuestion error: " + e.getMessage());
        }
        return list;
    }

    /** ✅ Cập nhật lại các lựa chọn (xóa cũ → thêm mới) */
    public static void updateOptions(int questionId, String[] newOptions) {
        deleteOptionsByQuestion(questionId);
        insertOptions(questionId, newOptions);
    }

    /** ✅ Xóa các lựa chọn thuộc 1 câu hỏi */
    public static void deleteOptionsByQuestion(int questionId) {
        String sql = "DELETE FROM options WHERE question_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, questionId);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[OptionDAO] deleteOptionsByQuestion error: " + e.getMessage());
        }
    }
}
