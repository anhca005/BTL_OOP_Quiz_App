package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.ClassDAO;
import com.example.easyquiz.model.Classroom;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.AlertUtils; // Added
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Added
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TeacherClassDetailsController {

    @FXML
    private Label classNameLabel;

    @FXML
    private TableView<User> studentTable;

    @FXML
    private TableColumn<User, String> colStudentName;

    @FXML
    private TableColumn<User, String> colStudentEmail;

    @FXML
    private TableColumn<User, Double> colAverageScore;

    private User currentUser; // The teacher viewing the details
    private Classroom currentClassroom;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setCurrentClassroom(Classroom classroom) {
        this.currentClassroom = classroom;
        classNameLabel.setText("Chi tiết Lớp học: " + classroom.getClass_name());
        loadStudentData();
    }

    @FXML
    private void initialize() {
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        colStudentEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAverageScore.setCellValueFactory(new PropertyValueFactory<>("average_score"));
    }

    private void loadStudentData() {
        if (currentClassroom != null) {
            List<User> students = ClassDAO.getStudentsInClass(currentClassroom.getClass_id());
            ObservableList<User> observableList = FXCollections.observableArrayList(students);
            studentTable.setItems(observableList);
        }
    }

    private TeacherMainController mainController;

    public void setMainController(TeacherMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleBackToClassManagement() {
        if (mainController != null) {
            mainController.showClass();
        }
    }
}