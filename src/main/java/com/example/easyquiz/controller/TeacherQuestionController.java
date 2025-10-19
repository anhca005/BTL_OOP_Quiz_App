package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.OptionDAO;
import com.example.easyquiz.data.dao.QuestionDAO;
import com.example.easyquiz.data.dao.QuizDAO;
import com.example.easyquiz.model.Question;
import com.example.easyquiz.model.Quiz;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TeacherQuestionController {

    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, Integer> idColumn;
    @FXML private TableColumn<Question, String> questionColumn;
    @FXML private TableColumn<Question, String> correctColumn;

    @FXML private TextField questionField;
    @FXML private TextField option1Field;
    @FXML private TextField option2Field;
    @FXML private TextField option3Field;
    @FXML private TextField option4Field;
    @FXML private TextField correctField;
    @FXML private TextField setNameField;

    @FXML private ComboBox<Quiz> quizComboBox; // danh sách quiz (bộ câu hỏi)

    private final ObservableList<Question> questionList = FXCollections.observableArrayList();
    private final ObservableList<Quiz> quizList = FXCollections.observableArrayList();

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = Session.getUser();
        loadQuizList();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionTextProperty());
        correctColumn.setCellValueFactory(cellData -> cellData.getValue().correctAnswerProperty());
        questionTable.setItems(questionList);

        // Khi chọn quiz => load câu hỏi
        if (quizComboBox != null) {
            quizComboBox.setOnAction(e -> {
                Quiz selectedQuiz = quizComboBox.getSelectionModel().getSelectedItem();
                if (selectedQuiz != null) {
                    loadQuestionsFromDB(selectedQuiz.getQuiz_id());
                }
            });
        }

        // Khi chọn câu hỏi => hiển thị lên form
        questionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                questionField.setText(newSelection.getQuestionText());
                String[] opts = newSelection.getOptions();
                if (opts != null && opts.length == 4) {
                    option1Field.setText(opts[0]);
                    option2Field.setText(opts[1]);
                    option3Field.setText(opts[2]);
                    option4Field.setText(opts[3]);
                }
                correctField.setText(newSelection.getCorrectAnswer());
            }
        });
    }

    /** 🔹 Load tất cả quiz của giáo viên */
    private void loadQuizList() {
        quizList.setAll(QuizDAO.getAllByTeacher(currentUser.getUser_id()));
        quizComboBox.setItems(quizList);
    }

    /** 🔹 Load tất cả câu hỏi trong quiz */
    private void loadQuestionsFromDB(int quizId) {
        List<Question> loaded = QuestionDAO.getQuestionsByQuiz(quizId);
        for (Question q : loaded) {
            List<String> options = OptionDAO.getOptionsByQuestion(q.getId());
            q.setOptions(options.toArray(new String[0]));
        }
        questionList.setAll(loaded);
    }

    /** 🔹 Thêm câu hỏi mới */
    @FXML
    private void handleAddQuestion(ActionEvent event) {
        Quiz selectedQuiz = quizComboBox.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn bộ câu hỏi trước!");
            return;
        }

        String text = questionField.getText().trim();
        String[] options = {
                option1Field.getText().trim(),
                option2Field.getText().trim(),
                option3Field.getText().trim(),
                option4Field.getText().trim()
        };
        String correct = correctField.getText().trim();

        if (text.isEmpty() || correct.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ nội dung câu hỏi và đáp án đúng!");
            return;
        }

        Question q = new Question(0, text, options, correct);
        int questionId = QuestionDAO.insertQuestion(selectedQuiz.getQuiz_id(), q);
        OptionDAO.insertOptions(questionId, options);

        q.setId(questionId);
        questionList.add(q);
        clearInputFields();
        showAlert(Alert.AlertType.INFORMATION, "Đã thêm câu hỏi mới!");
    }

    /** 🔹 Sửa câu hỏi */
    @FXML
    private void handleEditQuestion(ActionEvent event) {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn 1 câu hỏi để sửa!");
            return;
        }

        String text = questionField.getText().trim();
        String[] options = {
                option1Field.getText().trim(),
                option2Field.getText().trim(),
                option3Field.getText().trim(),
                option4Field.getText().trim()
        };
        String correct = correctField.getText().trim();

        if (text.isEmpty() || correct.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Câu hỏi và đáp án đúng không được để trống!");
            return;
        }

        selected.setQuestionText(text);
        selected.setOptions(options);
        selected.setCorrectAnswer(correct);

        QuestionDAO.updateQuestion(selected);
        OptionDAO.updateOptions(selected.getId(), options);
        questionTable.refresh();

        showAlert(Alert.AlertType.INFORMATION, "Đã cập nhật câu hỏi thành công!");
    }

    /** 🔹 Xóa câu hỏi */
    @FXML
    private void handleDeleteQuestion(ActionEvent event) {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn câu hỏi để xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa câu hỏi này?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            OptionDAO.deleteOptionsByQuestion(selected.getId());
            QuestionDAO.deleteQuestion(selected.getId());
            questionList.remove(selected);
        }
    }

    /** 🔹 Tạo bộ câu hỏi mới */
    @FXML
    private void handleSaveQuestionSet(ActionEvent event) {
        String title = setNameField.getText().trim();
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập tên bộ câu hỏi!");
            return;
        }

        int quizId = QuizDAO.insertQuiz(currentUser.getUser_id(), title, "Tạo trong app");
        loadQuizList();

        for (Quiz q : quizList) {
            if (q.getQuiz_id() == quizId) {
                quizComboBox.getSelectionModel().select(q);
                break;
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Đã tạo bộ câu hỏi mới!");
    }

    /** 🔹 Quay lại màn hình chính của giáo viên */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/teacher_home.fxml"));
            Parent root = loader.load();

            TeacherHomeController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) questionTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang Giáo viên");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Không thể quay lại màn hình chính!");
        }
    }

    /** 🔹 Tiện ích */
    private void clearInputFields() {
        questionField.clear();
        option1Field.clear();
        option2Field.clear();
        option3Field.clear();
        option4Field.clear();
        correctField.clear();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
