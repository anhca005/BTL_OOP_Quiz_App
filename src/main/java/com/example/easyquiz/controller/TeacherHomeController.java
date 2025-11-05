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
            welcomeLabel.setText("Chào mừng, giảng viên " + user.getUser_name() + "!");
        }
    }

    private TeacherMainController mainController;

    public void setMainController(TeacherMainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Chuyển sang trang quản lý câu hỏi khi bấm “Create quiz”
     */
    @FXML
    private void handleManageQuestions() {
        if (mainController != null) {
            mainController.showLibrary();
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
            stage.setTitle("Đăng nhập");
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
            stage.setTitle("Các bài kiểm tra của bạn");
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
            stage.setTitle("Quản lý lớp học");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
