package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    /**
     * Thêm kết quả làm bài mới vào bảng results.
     */
    public static void insertResult(int userId, long quizId, double score) {
        String sql = "INSERT INTO results (user_id, quiz_id, score, submitted_at) VALUES (?, ?, ?, datetime('now'))";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setLong(2, quizId);
            stmt.setDouble(3, score);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi thêm kết quả vào bảng results:");
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách lịch sử làm bài của một user.
     */
    public static List<Result> getResultsByUser(int userId) {
        List<Result> results = new ArrayList<>();

        String sql = """
                SELECT r.result_id,
                       q.title AS quiz_title,
                       r.score,
                       r.submitted_at
                FROM results r
                JOIN quizzes q ON r.quiz_id = q.quiz_id
                WHERE r.user_id = ?
                ORDER BY r.submitted_at DESC
                """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Result result = new Result(
                            rs.getInt("result_id"),
                            rs.getString("quiz_title"),
                            rs.getDouble("score"),
                            rs.getString("submitted_at")
                    );
                    results.add(result);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi truy vấn lịch sử làm bài của user_id = " + userId);
            e.printStackTrace();
        }

        return results;
    }
}
