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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
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

    private final ObservableList<Question> questionList = FXCollections.observableArrayList();
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionTextProperty());
        correctColumn.setCellValueFactory(cellData -> cellData.getValue().correctAnswerProperty());
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
    private void handleEditQuestion() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn câu hỏi để sửa!");
            return;
        }

        try {
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

            selected.setQuestionText(text);
            selected.setOptions(options);
            selected.setCorrectAnswer(correct);

            questionTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Đã cập nhật câu hỏi thành công!");
            clearInputFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi khi sửa câu hỏi!");
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

    // --- Lưu bộ câu hỏi ra file DOCX ---
    @FXML
    private void handleSaveQuestionSet() {
        try {
            if (questionList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Danh sách câu hỏi trống, không thể lưu!");
                return;
            }

            String setName = setNameField.getText().trim();
            if (setName.isEmpty()) {
                setName = "bo_cau_hoi";
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn nơi lưu bộ câu hỏi");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Word Document (*.docx)", "*.docx")
            );
            fileChooser.setInitialFileName(setName + ".docx");

            File file = fileChooser.showSaveDialog(questionTable.getScene().getWindow());
            if (file == null) return;

            try (XWPFDocument document = new XWPFDocument()) {
                XWPFParagraph title = document.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                titleRun.setText("BỘ CÂU HỎI TRẮC NGHIỆM");
                titleRun.setBold(true);
                titleRun.setFontSize(16);
                titleRun.addBreak();

                for (Question q : questionList) {
                    XWPFParagraph p = document.createParagraph();
                    XWPFRun r = p.createRun();
                    r.setText("Câu " + q.getId() + ": " + q.getQuestionText());
                    r.setBold(true);
                    r.addBreak();
                    r.setText("A. " + q.getOption1()); r.addBreak();
                    r.setText("B. " + q.getOption2()); r.addBreak();
                    r.setText("C. " + q.getOption3()); r.addBreak();
                    r.setText("D. " + q.getOption4()); r.addBreak();
                    r.setText("✅ Đáp án đúng: " + q.getCorrectAnswer());
                    r.addBreak(); r.addBreak();
                }

                try (FileOutputStream out = new FileOutputStream(file)) {
                    document.write(out);
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Đã lưu file DOCX thành công tại:\n" + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Không thể lưu file DOCX: " + e.getMessage());
        }
    }

    // --- Tải bộ câu hỏi từ file DOCX ---
    @FXML
    private void handleLoadQuestionSet() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn file DOCX cần tải");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Word Document (*.docx)", "*.docx")
            );

            File file = fileChooser.showOpenDialog(questionTable.getScene().getWindow());
            if (file == null) return;

            List<Question> loaded = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                List<XWPFParagraph> paragraphs = document.getParagraphs();
                int id = 1;
                String question = null;
                String[] options = new String[4];
                String correct = null;
                int optIndex = 0;

                for (XWPFParagraph para : paragraphs) {
                    String text = para.getText().trim();
                    if (text.startsWith("Câu")) {
                        question = text.substring(text.indexOf(":") + 1).trim();
                        optIndex = 0;
                    } else if (text.startsWith("A.")) {
                        options[0] = text.substring(2).trim();
                    } else if (text.startsWith("B.")) {
                        options[1] = text.substring(2).trim();
                    } else if (text.startsWith("C.")) {
                        options[2] = text.substring(2).trim();
                    } else if (text.startsWith("D.")) {
                        options[3] = text.substring(2).trim();
                    } else if (text.startsWith("✅ Đáp án đúng:")) {
                        correct = text.substring(text.indexOf(":") + 1).trim();
                        if (question != null && correct != null) {
                            loaded.add(new Question(id++, question, options.clone(), correct));
                            question = null;
                            correct = null;
                        }
                    }
                }
            }

            questionList.setAll(loaded);
            showAlert(Alert.AlertType.INFORMATION, "Đã tải file DOCX thành công:\n" + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Không thể tải file DOCX: " + e.getMessage());
        }
    }
}
