package com.example.olia.test.Controller;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.Entity.Student;
import com.example.olia.test.Entity.Test;
import com.example.olia.test.Entity.Theme;
import com.example.olia.test.Entity.User;

import java.util.List;


public interface IDatabaseHandler {
    void deleteAllTest();
    int getTableCount(String TITLE_TABLE);

    void addQuestion(Question question);
    Question getQuestion(int id);
    List<Question> getAllQuestions();
    String[] getArrQuestions();
    int getIdQuestion(Question question);
    int updateQuestion(Question question);
    void deleteQuestion(Question question);

    void addTheme(Theme theme);
    Theme getTheme(int id);
    int getIdTheme(String theme);
    List<Theme> getAllThemes();
    String[] getArrThemes();
    int updateTheme(Theme theme);
    void deleteTheme(Theme theme);

    int updateThemesInTest(Test test);

    void addAnswer(Answer answer);
    Answer getAnswer(int id);
    List<Answer> getAllAnswers();
    String[] getArrAnswers();
    List<String> getAnswerForQuestion(int numberQuestion, int valueTrue);
    int updateAnswer(Answer answer);
    void deleteAnswer(Answer answer);

    void addTest(Test test);
    List<Test> getAllTests();
    String[] getArrTests();
    int getIdTest(String test);
    Test getTest(int id);
    void deleteTest(Test test);
    int updateTest(Test test);

    void addStudent(Student student);
    boolean updateStudent(Student student);
    int getLastIdStudent();
    Student getStudent(int id);
    List<Student> getAllStudents();
    String[] getArrStudents();
    int getIdStudent(Student student);
    void deleteStudent(Student student);


    void addUser(User user);
    int getIdUser(String login);
    User getUser(int id);
    List<User> getAllUsers();
    void deleteUser(User user);
    int updateUser(User user);

}
