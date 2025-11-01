package com.example.easyquiz.controller;

import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentHomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label studentCodeLabel;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUser_name());
            if (currentUser.getStudentCode() != null && !currentUser.getStudentCode().isEmpty()) {
                studentCodeLabel.setText("(MSV: " + currentUser.getStudentCode() + ")");
            }
        } else {
            System.err.println("⚠️ currentUser NULL trong setCurrentUser()");
        }
    }

    /** 🔹 Nút: Làm bài kiểm tra */
    @FXML
    private void handleDoQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/student_quiz.fxml"));
            Parent root = loader.load();

            StudentQuizController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("🧠 Làm bài kiểm tra");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 🔹 Nút: Xem lịch sử làm bài */
    @FXML
    private void handleViewHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/student_history.fxml"));
            Parent root = loader.load();

            StudentHistoryController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("📜 Lịch sử làm bài");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 🔹 Nút: Đăng xuất */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
