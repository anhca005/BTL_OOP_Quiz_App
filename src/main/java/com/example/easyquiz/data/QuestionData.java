package com.example.easyquiz.data;

import com.example.easyquiz.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionData {
    private static final String FILE_PATH = "questions.json"; // Lưu cùng thư mục chạy app

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type listType = new TypeToken<List<Question>>(){}.getType();

    public static List<Question> loadQuestions() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            List<Question> questions = gson.fromJson(reader, listType);
            return questions != null ? questions : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void saveQuestions(List<Question> questions) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(questions, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
