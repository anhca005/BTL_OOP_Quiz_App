package com.example.easyquiz.controller;

import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentMainController {

    @FXML
    private BorderPane borderPane;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        showHome();
    }

    @FXML
    public void showHome() {
        loadPage("student_home");
    }

    @FXML
    private void showHistory() {
        loadPage("student_history");
    }

    @FXML
    private void showQuiz() {
        loadPage("student_quiz");
    }

    private void loadPage(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/" + fxmlName + ".fxml"));
            Parent page = loader.load();

            Object controller = loader.getController();
            if (controller instanceof StudentHomeController) {
                ((StudentHomeController) controller).setCurrentUser(currentUser);
            } else if (controller instanceof StudentHistoryController) {
                ((StudentHistoryController) controller).setCurrentUser(currentUser);
            } else if (controller instanceof StudentQuizController) {
                StudentQuizController studentQuizController = (StudentQuizController) controller;
                studentQuizController.setCurrentUser(currentUser);
                studentQuizController.setMainController(this);
            }

            borderPane.setCenter(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Session.clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
