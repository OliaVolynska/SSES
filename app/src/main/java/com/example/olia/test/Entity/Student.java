package com.example.olia.test.Entity;


public class Student {
    private int idStudent;
    private String fullName;
    private int idTest;
    private float result = 0.0f;
    private float mark;
    private String wrongQuestions = "";

    public Student(){}

    public Student(int idStudent, String fullName, int idTest, float mark, String wrongQuestions) {
        this.idStudent = idStudent;
        this.fullName = fullName;
        this.idTest = idTest;
        this.mark = mark;
        this.wrongQuestions = wrongQuestions;
    }

    public Student(String fullName, int idTest, float mark, String wrongQuestions) {
        this.fullName = fullName;
        this.idTest = idTest;
        this.mark = mark;
        this.wrongQuestions = wrongQuestions;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float weight) {
        this.result += weight;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getWrongQuestions() {
        return wrongQuestions;
    }

    public void setWrongQuestions(String wrongQuestions) {
        this.wrongQuestions = wrongQuestions;
    }

    public String[] getArrQuestions(){
        String strQuestions[] = wrongQuestions.split("\n");
        String questionsArr[] = new String[strQuestions.length];
        System.arraycopy(strQuestions, 0, questionsArr, 0, strQuestions.length);
        return questionsArr;
    }
}
