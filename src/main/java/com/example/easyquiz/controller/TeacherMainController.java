package com.example.easyquiz.controller;

import com.example.easyquiz.model.Classroom;
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
    public void showLibrary() {
        loadPage("teacher_library");
    }

    @FXML
    public void showClass() {
        loadPage("teacher_class_management");
    }

    public void loadPage(String fxmlName, Classroom classroom) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/easyquiz/" + fxmlName + ".fxml"));
            Parent page = loader.load();

            Object controller = loader.getController();
            if (controller instanceof TeacherHomeController) {
                TeacherHomeController teacherHomeController = (TeacherHomeController) controller;
                teacherHomeController.setCurrentUser(currentUser);
                teacherHomeController.setMainController(this);
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
            } else if (controller instanceof TeacherClassDetailsController) {
                TeacherClassDetailsController teacherClassDetailsController = (TeacherClassDetailsController) controller;
                teacherClassDetailsController.setCurrentUser(currentUser);
                teacherClassDetailsController.setMainController(this);
                if (classroom != null) {
                    teacherClassDetailsController.setCurrentClassroom(classroom);
                }
            }

            borderPane.setCenter(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlName) {
        loadPage(fxmlName, null);
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
