package com.example.easyquiz;

import com.example.easyquiz.data.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Paths;

import static com.example.easyquiz.data.DatabaseHelper.DB_FILE;

public class QuizApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(QuizApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 250);
        stage.setTitle("EasyQuiz - Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        DatabaseHelper.initDatabase();
        System.out.println("✅ Database file path: " + Paths.get(DB_FILE).toAbsolutePath());
    }

    public static void main(String[] args) {
        launch();
    }
}