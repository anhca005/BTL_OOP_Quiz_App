package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.QuizDAO;
import com.example.easyquiz.model.Quiz;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.AlertUtils;
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
import java.util.Optional;

public class TeacherLibraryController {

    @FXML
    private ListView<Quiz> quizListView;

    @FXML
    private Label teacherNameLabel;

    @FXML
    private Button deleteQuizButton;

    private User currentUser;
    private TeacherMainController mainController;

    public void setMainController(TeacherMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        // Disable delete button if no item is selected
        deleteQuizButton.disableProperty().bind(quizListView.getSelectionModel().selectedItemProperty().isNull());

        // Custom cell factory to display quiz title
        quizListView.setCellFactory(lv -> new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz quiz, boolean empty) {
                super.updateItem(quiz, empty);
                if (empty || quiz == null) {
                    setText(null);
                } else {
                    setText(quiz.getTitle() + " (ID: " + quiz.getQuiz_id() + ")");
                }
            }
        });
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        teacherNameLabel.setText("Thư viện của bạn, " + user.getUser_name());
        loadTeacherQuizzes();
    }

    private void loadTeacherQuizzes() {
        List<Quiz> quizList = QuizDAO.getAllByTeacher(currentUser.getUser_id());
        ObservableList<Quiz> observableList = FXCollections.observableArrayList(quizList);
        quizListView.setItems(observableList);
    }

    @FXML
    private void handleCreateNewQuiz() {
        if (mainController != null) {
            mainController.loadPage("teacher_questions", null);
        }
    }

    @FXML
    private void handleDeleteQuiz() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một bài kiểm tra để xóa.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Xác nhận xóa");
        confirmation.setHeaderText("Bạn có chắc chắn muốn xóa bài kiểm tra: " + selectedQuiz.getTitle() + "?");
        confirmation.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = QuizDAO.deleteQuiz(selectedQuiz.getQuiz_id());
            if (deleted) {
                loadTeacherQuizzes(); // Refresh the list
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Bài kiểm tra đã được xóa.");
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa bài kiểm tra.");
            }
        }
    }

    @FXML
    private void handleBack() {
        if (mainController != null) {
            mainController.showHome();
        }
    }
}
