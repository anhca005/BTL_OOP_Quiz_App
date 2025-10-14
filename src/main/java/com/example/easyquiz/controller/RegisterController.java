package com.example.easyquiz.controller;

import com.example.easyquiz.data.UserData;
import com.example.easyquiz.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RegisterController {

    @FXML
    private TextField usernameField;

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
        roleChoiceBox.getItems().addAll("student", "teacher");
        roleChoiceBox.setValue("student"); // mặc định

        registerBtn.setOnAction(this::handleRegister);
        backBtn.setOnAction(this::backToLogin);
    }

    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Lỗi", "Mật khẩu xác nhận không khớp");
            return;
        }

        List<User> users = UserData.loadUsers();
        boolean exists = users.stream().anyMatch(u -> u.getUsername().equals(username));
        if (exists) {
            showAlert("Lỗi", "Username đã tồn tại");
            return;
        }

        User newUser = new User(username, password, role);
        users.add(newUser);
        UserData.saveUsers(users);

        showAlert("Thành công", "Đăng ký thành công! Bạn có thể đăng nhập.");

        backToLogin(null); // quay lại login sau khi đăng ký
    }

    private void backToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
