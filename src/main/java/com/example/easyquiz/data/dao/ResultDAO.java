package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;

import java.sql.*;

public class ResultDAO {

    public static int addResult(int userId, Integer setId, double score) {
        String sql = "INSERT INTO results (user_id, set_id, score) VALUES (?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, userId);
            if (setId == null) p.setNull(2, Types.INTEGER); else p.setInt(2, setId);
            p.setDouble(3, score);
            p.executeUpdate();
            try (ResultSet g = p.getGeneratedKeys()) {
                if (g.next()) {
                    int resId = g.getInt(1);
                    // update average score
                    UserDAO.updateAverageScore(userId);
                    return resId;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return -1;
    }
}
