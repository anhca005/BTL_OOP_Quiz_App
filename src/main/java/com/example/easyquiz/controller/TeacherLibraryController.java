package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.QuizDAO;
import com.example.easyquiz.model.Quiz;
import com.example.easyquiz.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class TeacherLibraryController {

    @FXML
    private ListView<String> quizListView;

    @FXML
    private Label teacherNameLabel;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        teacherNameLabel.setText("Your quizzes, " + user.getUser_name());
        loadTeacherQuizzes();
    }

    private void loadTeacherQuizzes() {
        QuizDAO quizDAO = new QuizDAO();
        List<Quiz> quizList = quizDAO.getAllByTeacher(currentUser.getUser_id());

        ObservableList<String> displayList = FXCollections.observableArrayList();
        for (Quiz quiz : quizList) {
            displayList.add("[" + quiz.getQuiz_id() + "] ");
        }
        quizListView.setItems(displayList);
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_home.fxml"));
            Parent root = loader.load();

            TeacherHomeController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) quizListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Teacher Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
