package com.example.easyquiz.data;

import com.example.easyquiz.model.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> questions;

    public QuestionBank() {
        loadQuestions();
    }

    private void loadQuestions() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/questions.json");
            questions = mapper.readValue(is, new TypeReference<List<Question>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            questions = Collections.emptyList();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
