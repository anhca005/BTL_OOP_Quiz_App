package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.ClassDAO;
import com.example.easyquiz.model.Classroom;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.AlertUtils; // Added
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Added
import javafx.scene.Parent; // Added
import javafx.scene.Scene; // Added
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage; // Added
import javafx.util.Callback;

import java.io.IOException; // Added
import java.util.List;
import java.util.Optional;

public class TeacherClassManagementController {

    @FXML
    private TableView<Classroom> classTable;

    @FXML
    private TableColumn<Classroom, String> colClassName;

    @FXML
    private TableColumn<Classroom, String> colTeacher;

    @FXML
    private TableColumn<Classroom, Void> colActions;

    private User currentUser;
    private TeacherMainController mainController;

    public void setMainController(TeacherMainController mainController) {
        this.mainController = mainController;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadClassData();
    }

    @FXML
    private void initialize() {
        colClassName.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        // For colTeacher, we can't use PropertyValueFactory directly since Classroom has teacher_id, not teacher name.
        // We will handle this in loadClassData or use a custom cell factory if needed.
        // For now, let's just show the teacher's ID.
        colTeacher.setCellValueFactory(new PropertyValueFactory<>("teacher_id"));

        addButtonsToTable();
    }

    private void loadClassData() {
        if (currentUser != null) {
            List<Classroom> classes = ClassDAO.getClassesByTeacher(currentUser.getUser_id());
            ObservableList<Classroom> observableList = FXCollections.observableArrayList(classes);
            classTable.setItems(observableList);
        }
    }

    @FXML
    private void handleAddClass() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thêm Lớp học mới");
        dialog.setHeaderText("Nhập tên cho lớp học mới của bạn.");
        dialog.setContentText("Tên lớp:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(className -> {
            if (className.trim().isEmpty()) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, "Lỗi", "Tên lớp không được để trống.");
                return;
            }
            Classroom newClass = new Classroom();
            newClass.setClass_name(className.trim());
            newClass.setTeacher_id(currentUser.getUser_id());

            int newId = ClassDAO.addClass(newClass);
            if (newId > 0) {
                loadClassData(); // Refresh table
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm lớp học mới thành công!");
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm lớp học. Tên lớp có thể đã tồn tại.");
            }
        });
    }

    private void addButtonsToTable() {
        Callback<TableColumn<Classroom, Void>, TableCell<Classroom, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Classroom, Void> call(final TableColumn<Classroom, Void> param) {
                final TableCell<Classroom, Void> cell = new TableCell<>() {

                    private final Button viewButton = new Button("Xem chi tiết");

                    { 
                        viewButton.setOnAction(event -> {
                            Classroom classroom = getTableView().getItems().get(getIndex());
                            if (mainController != null) {
                                mainController.loadPage("teacher_class_details", classroom);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        }
                        else {
                            HBox buttons = new HBox(viewButton);
                            buttons.setSpacing(10);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };

        colActions.setCellFactory(cellFactory);
    }
    @FXML
    private void handleBack() {
        if (mainController != null) {
            mainController.showHome();
        }
    }
}