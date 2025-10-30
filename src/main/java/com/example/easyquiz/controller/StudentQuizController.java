package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.OptionDAO;
import com.example.easyquiz.data.dao.QuestionDAO;
import com.example.easyquiz.data.dao.ResultDAO;
import com.example.easyquiz.model.Question;
import com.example.easyquiz.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentQuizController {

    @FXML private TextField quizIdField;
    @FXML private Label questionLabel;
    @FXML private Label quizInfoLabel; // Thêm label để hiển thị quiz ID
    @FXML private VBox optionsBox;
    @FXML private Button nextButton;
    @FXML private Button startButton; // Để disable sau khi bắt đầu

    private User currentUser;
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private double score = 0;
    private ToggleGroup optionGroup;
    private long currentQuizId;
    private Button selectedOptionButton; // Thêm biến để theo dõi nút được chọn

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleStartQuiz() {
        try {
            currentQuizId = Long.parseLong(quizIdField.getText().trim());
            questions = QuestionDAO.getQuestionsByQuiz(currentQuizId);

            for (Question q : questions) {
                q.setOptions(OptionDAO.getOptionsByQuestion(q.getId()).toArray(new String[0]));
            }

            if (questions.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Không tìm thấy câu hỏi nào trong quiz này!");
                return;
            }

            // Khóa ô nhập quiz ID và nút bắt đầu
            quizIdField.setDisable(true);
            if (startButton != null) startButton.setDisable(true);

            // Hiển thị ID quiz đang làm
            if (quizInfoLabel != null)
                quizInfoLabel.setText("Quiz ID: " + currentQuizId);

            currentIndex = 0;
            score = 0;
            showQuestion();
            nextButton.setDisable(false);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Mã quiz ID không hợp lệ!");
        }
    }

    private void handleOptionSelection(Button selectedButton) {
        // Bỏ chọn nút cũ (nếu có)
        if (selectedOptionButton != null) {
            selectedOptionButton.getStyleClass().remove("selected-option");
        }
        // Chọn nút mới
        selectedOptionButton = selectedButton;
        selectedOptionButton.getStyleClass().add("selected-option");
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        questionLabel.setText("Câu " + (currentIndex + 1) + ": " + q.getQuestionText());
        optionsBox.getChildren().clear();

        // Thay thế RadioButton bằng Button
        selectedOptionButton = null; // Reset lựa chọn khi hiển thị câu hỏi mới
        String[] options = q.getOptions();
        char optionChar = 'a';
        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(options[i]);
            optionButton.setPrefWidth(Double.MAX_VALUE); // Nút chiếm toàn bộ chiều rộng
            optionButton.getStyleClass().add("option-button"); // Thêm style class để dễ dàng tùy chỉnh CSS
            optionButton.setUserData(String.valueOf(optionChar)); // Lưu trữ chữ cái lựa chọn vào UserData
            optionButton.setOnAction(event -> handleOptionSelection(optionButton));
            optionsBox.getChildren().add(optionButton);
            optionChar++;
        }
    }

    @FXML
    private void handleNext() {
        if (selectedOptionButton == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn 1 đáp án!");
            return;
        }

        String selectedOptionChar = (String) selectedOptionButton.getUserData();
        Question q = questions.get(currentIndex);

        // Lấy đáp án đúng từ Question model (đã được chuẩn hóa thành chữ thường)
        String correctAnswer = q.getCorrectAnswer();

        // So sánh đáp án đã chọn với đáp án đúng (không phân biệt hoa thường)
        if (selectedOptionChar.equalsIgnoreCase(correctAnswer)) {
            score += 10.0 / questions.size();
        }

        currentIndex++;

        // Nếu hết câu hỏi → kết thúc quiz
        if (currentIndex >= questions.size()) {
            nextButton.setDisable(true); // Không cho ấn tiếp theo nữa
            finishQuiz();
        } else {
            showQuestion();
        }
    }

    private void finishQuiz() {
        if (currentUser == null) {
            System.err.println("currentUser chưa được gán!");
            return;
        }

        ResultDAO.insertResult(currentUser.getUser_id(), currentQuizId, score);
        showAlert(Alert.AlertType.INFORMATION,
                String.format("Bạn đã hoàn thành quiz!\nĐiểm số: %.2f", score));
        System.out.println("Kết quả đã được lưu vào CSDL!");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/student_home.fxml"));
            Parent root = loader.load();

            StudentHomeController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) quizIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang Học sinh");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Không thể quay lại trang chính!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
