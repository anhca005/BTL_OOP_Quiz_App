package com.example.easyquiz.model;

import javafx.beans.property.*;

/**
 * Lớp đại diện cho một câu hỏi trắc nghiệm.
 * Hỗ trợ JavaFX Property để hiển thị và binding trong TableView.
 */
public class Question {

    private final IntegerProperty id;
    private final StringProperty questionText;
    private final StringProperty option1;
    private final StringProperty option2;
    private final StringProperty option3;
    private final StringProperty option4;
    private final StringProperty correctAnswer;

    // --- Constructor chính ---
    public Question(int id, String questionText, String[] options, String correctAnswer) {
        this.id = new SimpleIntegerProperty(id);
        this.questionText = new SimpleStringProperty(questionText);

        // Nếu options == null, thay bằng mảng rỗng
        if (options == null) {
            options = new String[0];
        }

        this.option1 = new SimpleStringProperty(options.length > 0 ? options[0] : "");
        this.option2 = new SimpleStringProperty(options.length > 1 ? options[1] : "");
        this.option3 = new SimpleStringProperty(options.length > 2 ? options[2] : "");
        this.option4 = new SimpleStringProperty(options.length > 3 ? options[3] : "");

        this.correctAnswer = new SimpleStringProperty(correctAnswer != null ? correctAnswer.toLowerCase() : "");
    }

    // --- Constructor mặc định (dành cho JSON hoặc tạo trống)
    public Question() {
        this(0, "", new String[]{"", "", "", ""}, "");
    }

    // --- Getter/Setter/Property ---
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getQuestionText() { return questionText.get(); }
    public void setQuestionText(String value) { questionText.set(value); }
    public StringProperty questionTextProperty() { return questionText; }

    public String getOption1() { return option1.get(); }
    public void setOption1(String value) { option1.set(value); }
    public StringProperty option1Property() { return option1; }

    public String getOption2() { return option2.get(); }
    public void setOption2(String value) { option2.set(value); }
    public StringProperty option2Property() { return option2; }

    public String getOption3() { return option3.get(); }
    public void setOption3(String value) { option3.set(value); }
    public StringProperty option3Property() { return option3; }

    public String getOption4() { return option4.get(); }
    public void setOption4(String value) { option4.set(value); }
    public StringProperty option4Property() { return option4; }

    public String getCorrectAnswer() { return correctAnswer.get(); }
    public void setCorrectAnswer(String value) { correctAnswer.set(value != null ? value.toLowerCase() : ""); }
    public StringProperty correctAnswerProperty() { return correctAnswer; }

    // --- Option Helpers ---
    public String[] getOptions() {
        return new String[]{
                getOption1(),
                getOption2(),
                getOption3(),
                getOption4()
        };
    }

    public void setOptions(String[] options) {
        if (options == null) return; // an toàn hơn
        if (options.length > 0) setOption1(options[0]);
        if (options.length > 1) setOption2(options[1]);
        if (options.length > 2) setOption3(options[2]);
        if (options.length > 3) setOption4(options[3]);
    }

    @Override
    public String toString() {
        return String.format("Question{id=%d, text='%s', correct='%s'}",
                getId(), getQuestionText(), getCorrectAnswer());
    }
}
