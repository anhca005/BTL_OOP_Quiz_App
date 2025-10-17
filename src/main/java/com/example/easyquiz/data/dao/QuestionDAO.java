package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    public static void saveQuestions(int setId, List<Question> questions) {
        String sql = "INSERT INTO questions (set_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            for (Question q : questions) {
                p.setInt(1, setId);
                p.setString(2, q.getQuestionText());
                String[] opts = q.getOptions();
                p.setString(3, opts.length>0?opts[0]:null);
                p.setString(4, opts.length>1?opts[1]:null);
                p.setString(5, opts.length>2?opts[2]:null);
                p.setString(6, opts.length>3?opts[3]:null);
                p.setString(7, q.getCorrectAnswer());
                p.addBatch();
            }
            p.executeBatch();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public static List<Question> getBySet(int setId) {
        List<Question> out = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE set_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, setId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    String[] opts = {
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d")
                    };
                    Question q = new Question(rs.getInt("question_id"), rs.getString("question_text"), opts, rs.getString("correct_answer"));
                    out.add(q);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }
}
