package com.example.easyquiz.model;

import javafx.beans.property.*;

/**
 * Lớp đại diện cho 1 Quiz (bộ câu hỏi).
 * Dùng JavaFX Property để hiển thị trong TableView.
 */
public class Quiz {

    private final LongProperty quiz_id;
    private final IntegerProperty user_id;
    private final StringProperty title;
    private final StringProperty description;
    private final IntegerProperty duration;
    private final StringProperty created_at;

    // --- Constructor ---
    public Quiz(long quiz_id, int user_id, String title, String description, int duration, String created_at) {
        this.quiz_id = new SimpleLongProperty(quiz_id);
        this.user_id = new SimpleIntegerProperty(user_id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.duration = new SimpleIntegerProperty(duration);
        this.created_at = new SimpleStringProperty(created_at);
    }

    // Constructor mặc định
    public Quiz() {
        this(0L, 0, "", "", 10, "");
    }

    // --- Getter / Setter / Property ---
    public long getQuiz_id() { return quiz_id.get(); }
    public void setQuiz_id(long value) { quiz_id.set(value); }
    public LongProperty quiz_idProperty() { return quiz_id; }

    public int getUser_id() { return user_id.get(); }
    public void setUser_id(int value) { user_id.set(value); }
    public IntegerProperty user_idProperty() { return user_id; }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }

    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }
    public StringProperty descriptionProperty() { return description; }

    public int getDuration() { return duration.get(); }
    public void setDuration(int value) { duration.set(value); }
    public IntegerProperty durationProperty() { return duration; }

    public String getCreated_at() { return created_at.get(); }
    public void setCreated_at(String value) { created_at.set(value); }
    public StringProperty created_atProperty() { return created_at; }

    /**
     * Mã hiển thị dạng userID_quizNumber (VD: 4_2)
     */
    public String getDisplayId() {
        return getUser_id() + "_" + getQuiz_id();
    }

    @Override
    public String toString() {
        return String.format("Quiz{id=%d, title='%s', desc='%s'}",
                getQuiz_id(), getTitle(), getDescription());
    }
}
