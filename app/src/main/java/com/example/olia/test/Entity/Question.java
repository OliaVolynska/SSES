package com.example.olia.test.Entity;

public class Question {
    private int number;
    private String textQuestion;
    private int idTheme;
    private float weight;

    public Question(){}

    public Question(int number, String question, int idTheme, float weight) {
        this.textQuestion = question;
        this.idTheme = idTheme;
        this.number = number;
        this.weight = weight;
    }

    public Question(String question, int idTheme, float weight) {
        this.textQuestion = question;
        this.idTheme = idTheme;
        this.weight = weight;
    }

    public int getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String question) {
        this.textQuestion = question;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Question questionObj = (Question) obj;
        return
                (textQuestion == questionObj.textQuestion
                || (textQuestion != null && textQuestion.equals(questionObj.getTextQuestion())))
                && (idTheme == questionObj.idTheme);
    }
}
