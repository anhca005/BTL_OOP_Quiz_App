package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.ClassDAO;
import com.example.easyquiz.data.dao.UserDAO;
import com.example.easyquiz.model.Classroom;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.AlertUtils;
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
    private TextField studentCodeField;

    @FXML
    private TextField classField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button registerBtn;

    @FXML
    private Button backBtn;

    @FXML
    private void initialize() {
        // Vai trò mặc định
        roleChoiceBox.getItems().addAll("Học sinh", "Giáo viên");
        roleChoiceBox.setValue("Học sinh");

        // Show/hide fields based on role
        studentCodeField.visibleProperty().bind(roleChoiceBox.valueProperty().isEqualTo("Học sinh"));
        studentCodeField.managedProperty().bind(studentCodeField.visibleProperty());
        classField.visibleProperty().bind(roleChoiceBox.valueProperty().isEqualTo("Học sinh"));
        classField.managedProperty().bind(classField.visibleProperty());

        registerBtn.setOnAction(this::handleRegister);
        backBtn.setOnAction(this::handleBackToLogin);
    }

    private void handleRegister(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();
        String role = roleChoiceBox.getValue();
        String studentCode = studentCodeField.getText().trim();
        String className = classField.getText().trim();

        // --- Kiểm tra hợp lệ ---
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ tất cả các trường.");
            return;
        }

        if ("Học sinh".equals(role) && (className.isEmpty() || studentCode.isEmpty())) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Học sinh phải nhập đầy đủ Mã sinh viên và Tên lớp học.");
            return;
        }

        if (!password.equals(confirm)) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Mật khẩu không khớp", "Vui lòng nhập lại mật khẩu.");
            return;
        }

        if (UserDAO.isEmailTaken(email)) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Email đã tồn tại", "Vui lòng chọn email khác.");
            return;
        }

        Integer classId = null;
        if ("Học sinh".equals(role)) {
            if (UserDAO.isStudentCodeTaken(studentCode)) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Mã sinh viên đã tồn tại", "Vui lòng chọn mã sinh viên khác.");
                return;
            }

            Classroom classroom = ClassDAO.findClassByName(className);
            if (classroom == null) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Lớp không tồn tại", "Lớp học \'" + className + "\' không tồn tại. Vui lòng kiểm tra lại.");
                return;
            }
            classId = classroom.getClass_id();
        }

        // --- Thêm user vào database ---
        User user = new User();
        user.setUser_name(name);
        user.setEmail(email);
        user.setPassword(password); // DAO will hash this
        user.setRole(role);
        user.setAverage_score(0.0);
        user.setClass_id(classId);
        if ("Học sinh".equals(role)) {
            user.setStudentCode(studentCode);
        }

        int newId = UserDAO.addUser(user);
        if (newId > 0) {
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký tài khoản thành công! Quay lại đăng nhập.");
            handleBackToLogin(event);
        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo tài khoản. Vui lòng thử lại.");
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
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại màn hình đăng nhập!");
        }
    }

}