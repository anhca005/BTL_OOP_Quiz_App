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

        correcttext.setText("Số câu đúng: " + correct);
        wrongtext.setText("Số câu sai: " + wrong);

        marks.setText(correct + "/" + totalQuestions);
        float correctf = (float) correct / totalQuestions;
        correct_progress.setProgress(correctf);

        float wrongf = (float) wrong / totalQuestions;
        wrong_progress.setProgress(wrongf);

        markstext.setText(correct + " Điểm");

        if (correctf < 0.2) {
            remark.setText("Ôi không..! Bạn đã trượt bài kiểm tra. Có vẻ như bạn cần cải thiện kiến ​​thức chung của mình. Hãy luyện tập hàng ngày!");
        } else if (correctf < 0.5) {
            remark.setText("Rất tiếc..! Bạn đã đạt được ít điểm hơn. Có vẻ như bạn cần cải thiện kiến ​​thức chung của mình.");
        } else if (correctf <= 0.7) {
            remark.setText("Tốt. Cải thiện một chút nữa có thể giúp bạn có kết quả tốt hơn. Thực hành là chìa khóa thành công.");
        } else if (correctf <= 0.9) {
            remark.setText("Xin chúc mừng! Chính sự chăm chỉ và quyết tâm của bạn đã giúp bạn ghi được điểm cao.");
        } else {
            remark.setText("Xin chúc mừng! Bạn đã vượt qua bài kiểm tra với số điểm tuyệt đối vì sự chăm chỉ và cống hiến của bạn trong học tập. Hãy tiếp tục phát huy!");
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
