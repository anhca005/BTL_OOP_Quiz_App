package com.example.easyquiz.utils;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void showAlert(String title, String msg) {
        showAlert(Alert.AlertType.INFORMATION, title, msg);
    }
}