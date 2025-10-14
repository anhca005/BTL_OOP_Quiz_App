package com.example.easyquiz.controller;

import com.example.easyquiz.model.Question;
import com.example.easyquiz.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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

    private ObservableList<Question> questionList = FXCollections.observableArrayList();
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void initialize() {
        // Cấu hình các cột TableView
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionTextProperty());
        correctColumn.setCellValueFactory(cellData -> cellData.getValue().correctAnswerProperty());

        // Mẫu dữ liệu ban đầu (demo)
        questionList.addAll(
                new Question(1, "Thủ đô của Việt Nam là?", new String[]{"Hà Nội", "TP.HCM", "Huế", "Đà Nẵng"}, "Hà Nội"),
                new Question(2, "2 + 2 = ?", new String[]{"3", "4", "5", "6"}, "4")
        );

        questionTable.setItems(questionList);

        questionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                questionField.setText(newSelection.getQuestionText());
                String[] opts = newSelection.getOptions();
                option1Field.setText(opts[0]);
                option2Field.setText(opts[1]);
                option3Field.setText(opts[2]);
                option4Field.setText(opts[3]);
                correctField.setText(newSelection.getCorrectAnswer());
            }
        });

    }

    @FXML
    private void handleAddQuestion() {
        try {
            int newId = questionList.size() + 1;
            String text = questionField.getText().trim();
            String[] options = {
                    option1Field.getText().trim(),
                    option2Field.getText().trim(),
                    option3Field.getText().trim(),
                    option4Field.getText().trim()
            };
            String correct = correctField.getText().trim();

            if (text.isEmpty() || correct.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin câu hỏi và đáp án đúng!");
                return;
            }

            Question q = new Question(newId, text, options, correct);
            questionList.add(q);
            clearInputFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi khi thêm câu hỏi!");
        }
    }

    @FXML
    private void handleDeleteQuestion() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            questionList.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn câu hỏi để xóa!");
        }
    }

    @FXML
    private void handleBack() {
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

    @FXML
    private void handleEditQuestion() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn câu hỏi để sửa!");
            return;
        }

        try {
            // Lấy dữ liệu mới từ các ô nhập
            String text = questionField.getText().trim();
            String[] options = {
                    option1Field.getText().trim(),
                    option2Field.getText().trim(),
                    option3Field.getText().trim(),
                    option4Field.getText().trim()
            };
            String correct = correctField.getText().trim();

            if (text.isEmpty() || correct.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin câu hỏi và đáp án đúng!");
                return;
            }

            // Cập nhật lại giá trị
            selected.setQuestionText(text);
            selected.setOptions(options);
            selected.setCorrectAnswer(correct);

            // Làm mới TableView
            questionTable.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Đã cập nhật câu hỏi thành công!");
            clearInputFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi khi sửa câu hỏi!");
        }
    }

}
