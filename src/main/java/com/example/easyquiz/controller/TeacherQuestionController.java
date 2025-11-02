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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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
    @FXML private CheckBox correctOptionA;
    @FXML private CheckBox correctOptionB;
    @FXML private CheckBox correctOptionC;
    @FXML private CheckBox correctOptionD;
    @FXML private TextField setNameField; // Đã thêm lại setNameField
    @FXML private Spinner<Integer> durationSpinner;

    @FXML private ComboBox<Quiz> quizComboBox; // danh sách quiz (bộ câu hỏi)

    private final ObservableList<Question> questionList = FXCollections.observableArrayList();
    private final ObservableList<Quiz> quizList = FXCollections.observableArrayList();

    private User currentUser;
    private TeacherMainController mainController;

    public void setMainController(TeacherMainController mainController) {
        this.mainController = mainController;
    }

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

        // Thiết lập listener cho các CheckBox để đảm bảo chỉ một được chọn
        setupCorrectOptionCheckBoxes();

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
                // Đặt CheckBox đáp án đúng
                clearCorrectOptionCheckBoxes(); // Xóa lựa chọn cũ trước
                String correctAns = newSelection.getCorrectAnswer();
                if (correctAns != null) {
                    switch (correctAns.toLowerCase()) {
                        case "a": correctOptionA.setSelected(true); break;
                        case "b": correctOptionB.setSelected(true); break;
                        case "c": correctOptionC.setSelected(true); break;
                        case "d": correctOptionD.setSelected(true); break;
                    }
                }
            }
        });
    }

    // Phương thức thiết lập các CheckBox đáp án đúng
    private void setupCorrectOptionCheckBoxes() {
        correctOptionA.setOnAction(event -> handleCorrectOptionCheckBox(correctOptionA));
        correctOptionB.setOnAction(event -> handleCorrectOptionCheckBox(correctOptionB));
        correctOptionC.setOnAction(event -> handleCorrectOptionCheckBox(correctOptionC));
        correctOptionD.setOnAction(event -> handleCorrectOptionCheckBox(correctOptionD));
    }

    // Phương thức xử lý sự kiện khi một CheckBox đáp án đúng được chọn
    private void handleCorrectOptionCheckBox(CheckBox selectedCheckBox) {
        if (selectedCheckBox.isSelected()) {
            if (selectedCheckBox != correctOptionA) correctOptionA.setSelected(false);
            if (selectedCheckBox != correctOptionB) correctOptionB.setSelected(false);
            if (selectedCheckBox != correctOptionC) correctOptionC.setSelected(false);
            if (selectedCheckBox != correctOptionD) correctOptionD.setSelected(false);
        }
    }

    /** Load tất cả quiz của giáo viên */
    private void loadQuizList() {
        quizList.setAll(QuizDAO.getAllByTeacher(currentUser.getUser_id()));
        quizComboBox.setItems(quizList);
    }

    /** Load tất cả câu hỏi trong quiz */
    private void loadQuestionsFromDB(long quizId) {
        List<Question> loaded = QuestionDAO.getQuestionsByQuiz(quizId);
        for (Question q : loaded) {
            List<String> options = OptionDAO.getOptionsByQuestion(q.getId());
            q.setOptions(options.toArray(new String[0]));
        }
        questionList.setAll(loaded);
    }

    /** Thêm câu hỏi mới */
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
        String correct = getSelectedCorrectOption();

        if (text.isEmpty() || correct.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ nội dung câu hỏi và chọn đáp án đúng!");
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

    /** Lấy đáp án đúng từ CheckBox đã chọn */
    private String getSelectedCorrectOption() {
        if (correctOptionA.isSelected()) return "a";
        if (correctOptionB.isSelected()) return "b";
        if (correctOptionC.isSelected()) return "c";
        if (correctOptionD.isSelected()) return "d";
        return ""; // Không có đáp án nào được chọn
    }

    /** Sửa câu hỏi */
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
        String correct = getSelectedCorrectOption(); // Lấy từ CheckBox

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

    /** Xóa câu hỏi */
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

    /** Tạo bộ câu hỏi mới */
    @FXML
    private void handleSaveQuestionSet(ActionEvent event) {
        String title = setNameField.getText().trim();
        int duration = durationSpinner.getValue();

        if (title.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập tên bộ câu hỏi!");
            return;
        }

        long newQuizId = QuizDAO.generateRandomQuizId(); // Tạo ID ngẫu nhiên
        long quizId = QuizDAO.insertQuiz(newQuizId, currentUser.getUser_id(), title, "Tạo trong app", duration);
        
        if (quizId != -1L) {
            loadQuizList();
            for (Quiz q : quizList) {
                if (q.getQuiz_id() == quizId) {
                    quizComboBox.getSelectionModel().select(q);
                    break;
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Đã tạo bộ câu hỏi mới với ID: " + quizId);
        } else {
            showAlert(Alert.AlertType.ERROR, "Không thể tạo bộ câu hỏi mới!");
        }
    }

    /** Quay lại màn hình chính của giáo viên */
    @FXML
    private void handleBack(ActionEvent event) {
        if (mainController != null) {
            mainController.showLibrary();
        }
    }

    @FXML
    private void handleCopyQuizId() {
        Quiz selectedQuiz = quizComboBox.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            String quizId = String.valueOf(selectedQuiz.getQuiz_id());
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(quizId);
            clipboard.setContent(content);
            showAlert(Alert.AlertType.INFORMATION, "Đã sao chép Quiz ID: " + quizId);
        } else {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn một Quiz để sao chép ID.");
        }
    }

    /** Tiện ích */
    private void clearInputFields() {
        questionField.clear();
        option1Field.clear();
        option2Field.clear();
        option3Field.clear();
        option4Field.clear();
        clearCorrectOptionCheckBoxes(); // Bỏ chọn tất cả CheckBox
    }

    // Phương thức để bỏ chọn tất cả các CheckBox đáp án đúng
    private void clearCorrectOptionCheckBoxes() {
        correctOptionA.setSelected(false);
        correctOptionB.setSelected(false);
        correctOptionC.setSelected(false);
        correctOptionD.setSelected(false);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
