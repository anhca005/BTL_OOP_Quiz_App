package com.example.easyquiz.controller;

import com.example.easyquiz.data.dao.ResultDAO;
import com.example.easyquiz.model.Result;
import com.example.easyquiz.model.User;
import com.example.easyquiz.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentHistoryController {

    @FXML private TableView<Result> resultTable;
    @FXML private TableColumn<Result, String> quizColumn;
    @FXML private TableColumn<Result, Double> scoreColumn;
    @FXML private TableColumn<Result, String> timeColumn;

    private User currentUser;

    /**
     * Gán thông tin user hiện tại và tải lịch sử làm bài.
     */
    public void setCurrentUser(User user) {
        this.currentUser = Session.getUser();
        loadHistory();
    }

    /**
     * Load danh sách kết quả làm bài của học sinh.
     */
    private void loadHistory() {
        if (currentUser == null) {
            System.err.println("⚠️ currentUser = null, không thể tải lịch sử!");
            return;
        }

        ObservableList<Result> results = FXCollections.observableArrayList(
                ResultDAO.getResultsByUser(currentUser.getUser_id())
        );

        quizColumn.setCellValueFactory(new PropertyValueFactory<>("quizTitle"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("submittedAt"));

        resultTable.setItems(results);
    }

    private StudentMainController mainController;

    public void setMainController(StudentMainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Xử lý khi nhấn nút "Quay lại" → quay về trang học sinh.
     */
    @FXML
    private void handleBack() {
        if (mainController != null) {
            mainController.showHome();
        }
    }
}
