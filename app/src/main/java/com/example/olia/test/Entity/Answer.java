package com.example.olia.test.Entity;

public class Answer {
    private int idAnswer;
    private String textAnswer;
    private int value;
    private int numberQuestion;

    public Answer() {
    }

    public Answer(String textAnswer, int value, int numberQuestion) {
        this.textAnswer = textAnswer;
        this.value = value;
        this.numberQuestion = numberQuestion;
    }

    public Answer(int idAnswer, String textAnswer, int value, int numberQuestion) {
        this.idAnswer = idAnswer;
        this.textAnswer = textAnswer;
        this.value = value;
        this.numberQuestion = numberQuestion;
    }

    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }
}
