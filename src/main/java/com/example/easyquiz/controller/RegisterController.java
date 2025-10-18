package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.UserDAO;
import com.example.easyquiz.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField nameField; // tên hiển thị (user_name)

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button registerBtn;

    @FXML
    private Button backBtn;

    @FXML
    private void initialize() {
        // Vai trò mặc định
        roleChoiceBox.getItems().addAll("student", "teacher");
        roleChoiceBox.setValue("student");

        registerBtn.setOnAction(this::handleRegister);
        backBtn.setOnAction(this::handleBackToLogin);
    }

    private void handleRegister(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();
        String role = roleChoiceBox.getValue();

        // --- Kiểm tra hợp lệ ---
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ tất cả các trường.");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert(Alert.AlertType.WARNING, "Mật khẩu không khớp", "Vui lòng nhập lại mật khẩu.");
            return;
        }

        if (UserDAO.isEmailTaken(email)) {
            showAlert(Alert.AlertType.ERROR, "Email đã tồn tại", "Vui lòng chọn email khác.");
            return;
        }

        // --- Thêm user vào database ---
        User user = new User();
        user.setUser_name(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setAverage_score(role.equals("student") ? 0.0 : 0);

        int newId = UserDAO.addUser(user);
        if (newId > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký tài khoản thành công! Quay lại đăng nhập.");
            handleBackToLogin(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo tài khoản. Vui lòng thử lại.");
        }
    }

    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại màn hình đăng nhập!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
