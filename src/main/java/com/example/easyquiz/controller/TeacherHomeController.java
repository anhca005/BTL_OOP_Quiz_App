package com.example.easyquiz.controller;

import com.example.easyquiz.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherHomeController {

    @FXML
    private Label welcomeLabel;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, teacher " + user.getUser_name() + "!");
        }
    }

    /**
     * Chuyển sang trang quản lý câu hỏi khi bấm “Create quiz”
     */
    @FXML
    private void handleManageQuestions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_questions.fxml"));
            Parent root = loader.load();

            // Lấy controller của trang tiếp theo và truyền thông tin user
            TeacherQuestionController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Question Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý đăng xuất (từ menu hoặc nút ▼ sau này)
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_library.fxml"));
            Parent root = loader.load();

            // Lấy controller của trang Library và truyền thông tin user
            TeacherLibraryController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Your Quizzes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClassManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_class_management.fxml"));
            Parent root = loader.load();

            // Lấy controller của trang Class Management và truyền thông tin user
            TeacherClassManagementController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Class Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
