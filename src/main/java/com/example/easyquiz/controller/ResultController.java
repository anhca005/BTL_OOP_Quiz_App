package com.example.easyquiz.controller;

import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultController {

    @FXML
    public Label remark, marks, markstext, correcttext, wrongtext;

    @FXML
    public ProgressIndicator correct_progress, wrong_progress;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setScore(double finalScore, int totalQuestions) {
        int correct = (int) Math.round((finalScore / 10.0) * totalQuestions);
        int wrong = totalQuestions - correct;

        correcttext.setText("Correct Answers : " + correct);
        wrongtext.setText("Incorrect Answers : " + wrong);

        marks.setText(correct + "/" + totalQuestions);
        float correctf = (float) correct / totalQuestions;
        correct_progress.setProgress(correctf);

        float wrongf = (float) wrong / totalQuestions;
        wrong_progress.setProgress(wrongf);

        markstext.setText(correct + " Marks Scored");

        if (correctf < 0.2) {
            remark.setText("Oh no..! You have failed the quiz. It seems that you need to improve your general knowledge. Practice daily!");
        } else if (correctf < 0.5) {
            remark.setText("Oops..! You have scored less marks. It seems like you need to improve your general knowledge.");
        } else if (correctf <= 0.7) {
            remark.setText("Good. A bit more improvement might help you to get better results. Practice is the key to success.");
        } else if (correctf <= 0.9) {
            remark.setText("Congratulations! Its your hardwork and determination which helped you to score good marks.");
        } else {
            remark.setText("Congratulations! You have passed the quiz with full marks because of your hardwork and dedication towards studies. Keep it up!");
        }
    }

    private StudentMainController mainController;

    public void setMainController(StudentMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleBackToHome() {
        if (mainController != null) {
            mainController.showHome();
        }
    }
}
