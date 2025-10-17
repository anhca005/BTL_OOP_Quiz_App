module com.example.easyquiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.google.gson;
    requires org.apache.poi.ooxml;

    opens com.example.easyquiz to javafx.fxml;
    exports com.example.easyquiz;

    exports com.example.easyquiz.data;
    opens com.example.easyquiz.data to javafx.fxml;

    // mở package controller cho FXML
    exports com.example.easyquiz.controller;
    opens com.example.easyquiz.controller to javafx.fxml;

    exports com.example.easyquiz.model;           // thêm
    opens com.example.easyquiz.model to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.easyquiz.data.dao;
    opens com.example.easyquiz.data.dao to javafx.fxml; // thêm
}
