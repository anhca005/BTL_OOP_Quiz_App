package com.example.easyquiz.data.dao;

import com.example.easyquiz.data.DatabaseHelper;
import com.example.easyquiz.model.Classroom;
import com.example.easyquiz.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    public static int addClass(Classroom classroom) {
        String sql = "INSERT INTO classes (class_name, teacher_id) VALUES (?, ?)";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, classroom.getClass_name());
            p.setInt(2, classroom.getTeacher_id());
            p.executeUpdate();
            try (ResultSet g = p.getGeneratedKeys()) {
                if (g.next()) return g.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static Classroom findClassByName(String className) {
        String sql = "SELECT * FROM classes WHERE class_name = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, className);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    Classroom classroom = new Classroom();
                    classroom.setClass_id(rs.getInt("class_id"));
                    classroom.setClass_name(rs.getString("class_name"));
                    classroom.setTeacher_id(rs.getInt("teacher_id"));
                    return classroom;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<Classroom> getClassesByTeacher(int teacherId) {
        List<Classroom> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes WHERE teacher_id = ?";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, teacherId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Classroom classroom = new Classroom();
                    classroom.setClass_id(rs.getInt("class_id"));
                    classroom.setClass_name(rs.getString("class_name"));
                    classroom.setTeacher_id(rs.getInt("teacher_id"));
                    classes.add(classroom);
                } 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return classes;
    }

    public static List<User> getStudentsInClass(int classId) {
        List<User> students = new ArrayList<>();
        String sql = "SELECT user_id, user_name, email, role, average_score FROM users WHERE class_id = ? AND role = 'student'";
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, classId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    User student = new User();
                    student.setUser_id(rs.getInt("user_id"));
                    student.setUser_name(rs.getString("user_name"));
                    student.setEmail(rs.getString("email"));
                    student.setRole(rs.getString("role"));
                    student.setAverage_score(rs.getDouble("average_score"));
                    // Password and class_id are not retrieved here for security/relevance
                    students.add(student);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students;
    }
}