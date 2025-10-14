package com.example.easyquiz.controller;

import com.example.easyquiz.data.UserData;
import com.example.easyquiz.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đủ username và password");
            return;
        }

        List<User> users = UserData.loadUsers();

        User found = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (found != null) {
            showAlert("Thành công", "Xin chào " + found.getUsername() + " (" + found.getRole() + ")");

            // 👉 Chuyển màn hình theo role
            try {
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                FXMLLoader loader;
                if ("teacher".equalsIgnoreCase(found.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_home.fxml"));
                    Parent root = loader.load();
                    TeacherHomeController controller = loader.getController();
                    controller.setCurrentUser(found);
                    stage.setScene(new Scene(root));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/student_home.fxml"));
                    Parent root = loader.load();
                    StudentHomeController controller = loader.getController();
                    controller.setCurrentUser(found);
                    stage.setScene(new Scene(root));
                }

//                Scene scene = new Scene(loader.load());
//                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showAlert("Sai thông tin", "Username hoặc password không đúng");
        }
    }

    private void openRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow(); // Lấy stage hiện tại
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng ký tài khoản");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("⚠️ Không thể tải register.fxml. Kiểm tra lại đường dẫn!");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
