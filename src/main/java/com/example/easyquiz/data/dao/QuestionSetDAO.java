package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionSetDAO {
    public static int createSet(String setName, Integer createdBy) {
        String sql = "INSERT INTO question_sets (set_name, created_by) VALUES (?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, setName);
            if (createdBy == null) p.setNull(2, Types.INTEGER);
            else p.setInt(2, createdBy);
            p.executeUpdate();
            try (ResultSet g = p.getGeneratedKeys()) {
                if (g.next()) return g.getInt(1);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return -1;
    }

    public static List<String[]> listSets() {
        String sql = "SELECT set_id, set_name FROM question_sets";
        List<String[]> out = new ArrayList<>();
        try (Connection c = DatabaseHelper.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                out.add(new String[]{String.valueOf(rs.getInt("set_id")), rs.getString("set_name")});
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }
}
