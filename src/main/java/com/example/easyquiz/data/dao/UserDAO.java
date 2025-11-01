package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.PasswordUtils;

import java.sql.*;

public class UserDAO {

    public static boolean isEmailTaken(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isStudentCodeTaken(String studentCode) {
        String sql = "SELECT 1 FROM users WHERE student_code = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, studentCode);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static int addUser(User user) {
        String sql = "INSERT INTO users (user_name, email, password, role, student_code, average_score, class_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, user.getUser_name());
            p.setString(2, user.getEmail());
            // Hash the password before saving
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
            p.setString(3, hashedPassword);
            p.setString(4, user.getRole());
            p.setString(5, user.getStudentCode());
            p.setDouble(6, user.getAverage_score());
            if (user.getClass_id() != null) {
                p.setInt(7, user.getClass_id());
            } else {
                p.setNull(7, Types.INTEGER);
            }
            p.executeUpdate();
            try (ResultSet g = p.getGeneratedKeys()) {
                if (g.next()) return g.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static User authenticate(String email, String plainPassword) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    if (PasswordUtils.verifyPassword(plainPassword, storedHashedPassword)) {
                        User u = new User();
                        u.setUser_id(rs.getInt("user_id"));
                        u.setUser_name(rs.getString("user_name"));
                        u.setStudentCode(rs.getString("student_code"));
                        u.setEmail(rs.getString("email"));
                        u.setPassword(storedHashedPassword); // Store the hashed password in the user object
                        u.setRole(rs.getString("role"));
                        u.setAverage_score(rs.getDouble("average_score"));
                        u.setClass_id((Integer) rs.getObject("class_id")); // Retrieve class_id
                        return u;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void updateAverageScore(int userId) {
        String sql = "UPDATE users SET average_score = (" +
                "SELECT AVG(score) FROM results WHERE user_id = ?" +
                ") WHERE user_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, userId);
            p.setInt(2, userId);
            p.executeUpdate();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
