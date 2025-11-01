package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.OptionDAO;
import com.example.easyquiz.data.dao.QuestionDAO;
import com.example.easyquiz.data.dao.QuizDAO;
import com.example.easyquiz.data.dao.ResultDAO;
import com.example.easyquiz.model.Question;
import com.example.easyquiz.model.Quiz;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.AlertUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class StudentQuizController {

    @FXML private TextField quizIdField;
    @FXML private Label questionLabel;
    @FXML private Label quizInfoLabel;
    @FXML private Label timerLabel;
    @FXML private VBox optionsBox;
    @FXML private Button nextButton;
    @FXML private Button prevButton;
    @FXML private Button startButton;
    @FXML private Button submitButton;
    @FXML private VBox quizContentPane;
    @FXML private VBox navigationPane;
    @FXML private FlowPane questionNavPane;

    private User currentUser;
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private long currentQuizId;

    // State management
    private Map<Integer, String> userAnswers;
    private List<Button> questionNavButtons;
    private Timeline timeline;
    private IntegerProperty timeSeconds;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void initialize() {
        quizContentPane.setVisible(false);
        navigationPane.setVisible(false);
        timerLabel.setVisible(false);
    }

    @FXML
    private void handleStartQuiz() {
        try {
            currentQuizId = Long.parseLong(quizIdField.getText().trim());
            Quiz quiz = QuizDAO.getQuizById(currentQuizId);

            if (quiz == null) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", "Quiz ID not found.");
                return;
            }

            questions = QuestionDAO.getQuestionsByQuiz(currentQuizId);

            if (questions.isEmpty()) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", "No questions found for this quiz ID.");
                return;
            }

            for (Question q : questions) {
                q.setOptions(OptionDAO.getOptionsByQuestion(q.getId()).toArray(new String[0]));
            }

            userAnswers = new HashMap<>();
            questionNavButtons = new ArrayList<>();
            currentIndex = 0;

            quizIdField.setDisable(true);
            startButton.setDisable(true);
            quizInfoLabel.setText("Quiz ID: " + currentQuizId);
            quizContentPane.setVisible(true);
            navigationPane.setVisible(true);
            timerLabel.setVisible(true);

            createQuestionNavButtons();
            displayQuestion();
            startTimer(quiz.getDuration());

        } catch (NumberFormatException e) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Invalid ID", "Invalid Quiz ID format.");
        }
    }

    private void startTimer(int minutes) {
        timeSeconds = new SimpleIntegerProperty(minutes * 60);

        timeSeconds.addListener((obs, oldVal, newVal) -> {
            int seconds = newVal.intValue();
            int mins = seconds / 60;
            int secs = seconds % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", mins, secs));
        });

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds.set(timeSeconds.get() - 1);
            if (timeSeconds.get() <= 0) {
                timeline.stop();
                finishQuiz();
            }
        }));
        timeline.playFromStart();
    }

    private void createQuestionNavButtons() {
        questionNavPane.getChildren().clear();
        questionNavButtons.clear();
        for (int i = 0; i < questions.size(); i++) {
            Button navButton = new Button(String.valueOf(i + 1));
            navButton.getStyleClass().add("question-nav-button");
            final int questionIndex = i;
            navButton.setOnAction(event -> navigateToQuestion(questionIndex));
            questionNavPane.getChildren().add(navButton);
            questionNavButtons.add(navButton);
        }
    }

    private void navigateToQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            currentIndex = questionIndex;
            displayQuestion();
        }
    }

    private void displayQuestion() {
        Question q = questions.get(currentIndex);
        questionLabel.setText("Câu " + (currentIndex + 1) + ": " + q.getQuestionText());
        optionsBox.getChildren().clear();

        String previouslySelected = userAnswers.get(currentIndex);

        char optionChar = 'a';
        for (String optionText : q.getOptions()) {
            Button optionButton = new Button(optionText);
            optionButton.setPrefWidth(Double.MAX_VALUE);
            optionButton.getStyleClass().add("option-button");
            String optionId = String.valueOf(optionChar);
            optionButton.setUserData(optionId);

            if (optionId.equals(previouslySelected)) {
                optionButton.getStyleClass().add("selected-option");
            }

            optionButton.setOnAction(event -> handleOptionSelection(optionButton));
            optionsBox.getChildren().add(optionButton);
            optionChar++;
        }

        prevButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == questions.size() - 1);
    }

    private void handleOptionSelection(Button selectedButton) {
        String selectedOptionId = (String) selectedButton.getUserData();
        userAnswers.put(currentIndex, selectedOptionId);

        for (Object node : optionsBox.getChildren()) {
            ((Button) node).getStyleClass().remove("selected-option");
        }
        selectedButton.getStyleClass().add("selected-option");

        updateQuestionNavButtonStyle(currentIndex);
    }

    private void updateQuestionNavButtonStyle(int questionIndex) {
        Button navButton = questionNavButtons.get(questionIndex);
        if (userAnswers.containsKey(questionIndex)) {
            if (!navButton.getStyleClass().contains("question-answered")) {
                navButton.getStyleClass().add("question-answered");
            }
        } else {
            navButton.getStyleClass().remove("question-answered");
        }
    }

    @FXML
    private void handleNext() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            displayQuestion();
        }
    }

    @FXML
    private void handlePrev() {
        if (currentIndex > 0) {
            currentIndex--;
            displayQuestion();
        }
    }

    @FXML
    private void handleSubmitEarly() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Submission");
        confirmation.setHeaderText("Are you sure you want to submit your quiz?");
        confirmation.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (timeline != null) {
                timeline.stop();
            }
            finishQuiz();
        }
    }

    private void finishQuiz() {
        if (timeline != null) {
            timeline.stop();
        }
        double finalScore = calculateFinalScore();
        ResultDAO.insertResult(currentUser.getUser_id(), currentQuizId, finalScore);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/result.fxml"));
            Parent root = loader.load();

            ResultController controller = loader.getController();
            controller.setScore(finalScore, questions.size());
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) questionLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz Result");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Could not load the result screen.");
        }
    }

    private double calculateFinalScore() {
        double score = 0;
        double pointsPerQuestion = 10.0 / questions.size();
        for (int i = 0; i < questions.size(); i++) {
            String userAnswer = userAnswers.get(i);
            String correctAnswer = questions.get(i).getCorrectAnswer();
            if (userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer)) {
                score += pointsPerQuestion;
            }
        }
        return score;
    }

    @FXML
    private void handleBack() {
        if (timeline != null) {
            timeline.stop();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/student_home.fxml"));
            Parent root = loader.load();

            StudentHomeController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) quizIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Could not go back to home screen.");
        }
    }
}
