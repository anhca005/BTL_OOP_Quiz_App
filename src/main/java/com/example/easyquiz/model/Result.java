package com.example.easyquiz.model;

public class Result {
    private int resultId;
    private String quizTitle;
    private double score;
    private String submittedAt;

    public Result(int resultId, String quizTitle, double score, String submittedAt) {
        this.resultId = resultId;
        this.quizTitle = quizTitle;
        this.score = score;
        this.submittedAt = submittedAt;
    }

    public int getResultId() { return resultId; }
    public String getQuizTitle() { return quizTitle; }
    public double getScore() { return score; }
    public String getSubmittedAt() { return submittedAt; }

    @Override
    public String toString() {
        return quizTitle + " - " + score + " điểm (" + submittedAt + ")";
    }
}
