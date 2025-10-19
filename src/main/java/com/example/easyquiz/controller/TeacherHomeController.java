package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.QuizDAO;
import com.example.easyquiz.model.Quiz;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TeacherHomeController {

    @FXML private Label welcomeLabel;
    @FXML private Label teacherIdLabel;
    @FXML private TableView<Quiz> quizTable;
    @FXML private TableColumn<Quiz, Integer> colQuizId;
    @FXML private TableColumn<Quiz, String> colTitle;
    @FXML private TableColumn<Quiz, String> colDescription;

    private final ObservableList<Quiz> quizList = FXCollections.observableArrayList();
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = Session.getUser();
        welcomeLabel.setText("Xin chào, Giáo viên: " + user.getUser_name());
        teacherIdLabel.setText("ID: " + user.getUser_id());
        loadQuizList();
    }

    @FXML
    private void initialize() {
        colQuizId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        quizTable.getItems().indexOf(cellData.getValue()) + 1
                ).asObject()
        );
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        quizTable.setItems(quizList);
    }

    private void loadQuizList() {
        if (currentUser == null) return;
        List<Quiz> quizzes = QuizDAO.getAllByTeacher(currentUser.getUser_id());
        quizList.setAll(quizzes);
    }

    @FXML
    private void handleAddQuiz() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thêm Quiz mới");
        dialog.setHeaderText("Nhập tiêu đề cho quiz:");
        dialog.setContentText("Tiêu đề:");
        dialog.showAndWait().ifPresent(title -> {
            int id = QuizDAO.insertQuiz(currentUser.getUser_id(), title, "Tạo trực tiếp bởi giáo viên");
            if (id > 0) {
                loadQuizList();
                showAlert(Alert.AlertType.INFORMATION, "Đã thêm quiz mới thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Không thể thêm quiz!");
            }
        });
    }

    @FXML
    private void handleManageQuestions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_questions.fxml"));
            Parent root = loader.load();

            // Gửi thông tin giáo viên sang màn câu hỏi
            TeacherQuestionController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) quizTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quản lý Câu hỏi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Không thể mở giao diện quản lý câu hỏi!");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) quizTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
