package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.UserDAO;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private void initialize() {
        loginBtn.setOnAction(this::handleLogin);
        registerBtn.setOnAction(this::openRegister);
        loginBtn.setDefaultButton(true);
    }

    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ email và mật khẩu!");
            return;
        }

        User user = UserDAO.findByEmailAndPassword(email, password);
        if (user != null) {
            showAlert("Thành công", "Xin chào " + user.getUser_name() + " (" + user.getRole() + ")");
            Session.setUser(user);

            try {
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                FXMLLoader loader;

                if ("teacher".equalsIgnoreCase(user.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_home.fxml"));
                    Parent root = loader.load();
                    TeacherHomeController controller = loader.getController();
                    controller.setCurrentUser(user);
                    stage.setScene(new Scene(root));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/student_home.fxml"));
                    Parent root = loader.load();
                    StudentHomeController controller = loader.getController();
                    controller.setCurrentUser(user);
                    stage.setScene(new Scene(root));
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể tải màn hình chính!");
            }

        } else {
            showAlert("Sai thông tin", "Email hoặc mật khẩu không đúng!");
        }
    }

    private void openRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng ký tài khoản");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải màn hình đăng ký!");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
