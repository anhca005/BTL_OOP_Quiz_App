package com.example.easyquiz.controller;

import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherMainController {

    @FXML
    private BorderPane borderPane;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // You can now use the currentUser object to pass to other controllers if needed
        showHome();
    }

    @FXML
    public void showHome() {
        loadPage("teacher_home");
    }

    @FXML
    private void showLibrary() {
        loadPage("teacher_library");
    }

    @FXML
    private void showClass() {
        loadPage("teacher_class_management");
    }

    private void loadPage(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/" + fxmlName + ".fxml"));
            Parent page = loader.load();

            // Pass the current user to the loaded controller if it needs it
            Object controller = loader.getController();
            if (controller instanceof TeacherHomeController) {
                ((TeacherHomeController) controller).setCurrentUser(currentUser);
            } else if (controller instanceof TeacherLibraryController) {
                TeacherLibraryController teacherLibraryController = (TeacherLibraryController) controller;
                teacherLibraryController.setCurrentUser(currentUser);
                teacherLibraryController.setMainController(this);
            } else if (controller instanceof TeacherClassManagementController) {
                TeacherClassManagementController teacherClassManagementController = (TeacherClassManagementController) controller;
                teacherClassManagementController.setCurrentUser(currentUser);
                teacherClassManagementController.setMainController(this);
            } else if (controller instanceof TeacherQuestionController) {
                TeacherQuestionController teacherQuestionController = (TeacherQuestionController) controller;
                teacherQuestionController.setCurrentUser(currentUser);
                teacherQuestionController.setMainController(this);
            }

            borderPane.setCenter(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Session.clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
