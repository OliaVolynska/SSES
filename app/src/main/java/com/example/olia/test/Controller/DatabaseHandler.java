package com.example.olia.test.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.Entity.Student;
import com.example.olia.test.Entity.Test;
import com.example.olia.test.Entity.Theme;
import com.example.olia.test.Entity.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SystemTestsDB";

    public static final String TABLE_QUESTION = "QuestionTable";
    public static final String KEY_ID_QUESTION = "_idQuestion";
    public static final String KEY_TEXT_QUESTION = "question";
    public static final String KEY_THEME_OF_QUESTION = "theme";
    public static final String KEY_WEIGHT_QUESTION = "weight";

    public static final String TABLE_ANSWER = "AnswerTable";
    public static final String KEY_ID_ANSWER = "_idAnswer";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_TRUE = "correct";
    public static final String KEY_NUMBER_QUESTION = "numberQuestion";

    public static final String TABLE_THEME = "ThemeTable";
    public static final String KEY_ID_THEME = "_idTheme";
    public static final String KEY_TITLE_THEME = "titleTheme";

    public static final String TABLE_TEST = "TestTable";
    public static final String KEY_ID_TEST = "_idTest";
    public static final String KEY_TEST = "test";
    public static final String KEY_MARK = "mark";
    public static final String KEY_THEMES = "themes";

    public static final String TABLE_STUDENT = "StudentTable";
    public static final String KEY_ID_STUDENT = "_idStudent";
    public static final String KEY_NAME_STUDENT = "nameStudent";
    public static final String KEY_ID_TEST_STUDENT = "testStudent";
    public static final String KEY_MARK_STUDENT = "markStudent";
    public static final String KEY_WRONG_QUESTIONS = "wrongQuestions";

    public static final String TABLE_USER = "UserTable";
    public static final String KEY_ID_USER = "idUser";
    public static final String KEY_LOGIN_USER = "loginUser";
    public static final String KEY_NAME_USER = "nameUser";
    public static final String KEY_PASS_USER = "passUser";
    public static final String KEY_RANK = "rankUser";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_QUESTION + "(" + KEY_ID_QUESTION + " integer primary key," +
                KEY_TEXT_QUESTION + " text," + KEY_THEME_OF_QUESTION + " integer," +
                KEY_WEIGHT_QUESTION + " float" + ")");

        db.execSQL("create table " + TABLE_ANSWER + "(" + KEY_ID_ANSWER + " integer primary key," +
                KEY_ANSWER + " text," + KEY_TRUE + " integer," +
                KEY_NUMBER_QUESTION + " integer" + ")");

        db.execSQL("create table " + TABLE_THEME + "(" + KEY_ID_THEME + " integer primary key," +
                KEY_TITLE_THEME + " text" + ")");

        db.execSQL("create table " + TABLE_TEST + "(" + KEY_ID_TEST + " integer primary key," +
                KEY_TEST + " text," + KEY_MARK + " float," + KEY_THEMES + " text" + ")");

        db.execSQL("create table " + TABLE_STUDENT + "(" + KEY_ID_STUDENT + " integer primary key," +
                KEY_NAME_STUDENT + " text," + KEY_ID_TEST_STUDENT + " integer," +
                KEY_MARK_STUDENT + " float," + KEY_WRONG_QUESTIONS + " text" + ")");

        db.execSQL("create table " + TABLE_USER + "(" + KEY_ID_USER + " integer primary key," +
                KEY_LOGIN_USER + " text," + KEY_NAME_USER + " text," +
                KEY_PASS_USER + " text," + KEY_RANK + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_QUESTION);
        db.execSQL("drop table if exists " + TABLE_ANSWER);
        db.execSQL("drop table if exists " + TABLE_THEME);
        db.execSQL("drop table if exists " + TABLE_TEST);
        db.execSQL("drop table if exists " + TABLE_STUDENT);
        db.execSQL("drop table if exists " + TABLE_USER);

        onCreate(db);
    }

    @Override
    public int getTableCount(String TITLE_TABLE) {
        String countQuery = "SELECT * FROM " + TITLE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void deleteAllTest() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEST, null, null);
        db.delete(TABLE_ANSWER, null, null);
        db.delete(TABLE_QUESTION, null, null);
        db.delete(TABLE_STUDENT, null, null);
        db.delete(TABLE_THEME, null, null);
        db.close();
    }

    @Override
    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TEXT_QUESTION, question.getTextQuestion());
        contentValues.put(KEY_THEME_OF_QUESTION, question.getIdTheme());
        contentValues.put(KEY_WEIGHT_QUESTION, question.getWeight());

        db.insert(TABLE_QUESTION, null, contentValues);
        db.close();
    }

    @Override
    public int getIdQuestion(Question question){
        List<Question> questionList = getAllQuestions();
        int id = -1;
        for (Question item:questionList) {
            if(item.equals(question)){
                id = item.getNumber();
            }
        }
        return id;
    }

    @Override
    public Question getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTION, new String[] {
                KEY_ID_QUESTION, KEY_TEXT_QUESTION, KEY_THEME_OF_QUESTION, KEY_WEIGHT_QUESTION},
                KEY_ID_QUESTION + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Question question = new Question(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), Float.parseFloat(cursor.getString(3)));
        return question;
    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<Question>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Question question = new Question();
                question.setNumber(Integer.parseInt(cursor.getString(0)));
                question.setTextQuestion(cursor.getString(1));
                question.setIdTheme(Integer.parseInt(cursor.getString(2)));
                question.setWeight(Float.parseFloat(cursor.getString(3)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        return questionList;
    }

    @Override
    public String[] getArrQuestions() {
        String[] questionArr = new String[getAllQuestions().size()];
        int indexTextQuestion = 0;
        for (Question itemQuestion : getAllQuestions()) {
            questionArr[indexTextQuestion] = itemQuestion.getTextQuestion();
            indexTextQuestion++;
        }
        return questionArr;
    }

    public List<Question> getQuestionForTheme(int idTheme){
        List<Question> questionsListForTheme = new ArrayList<Question>();
        List<Question> allQuestionsList = getAllQuestions();
        for (Question item:allQuestionsList) {
            if(item.getIdTheme() == idTheme){
                questionsListForTheme.add(item);
            }
        }
        return questionsListForTheme;
    }

    public List<Question> getQuestionForTest(String[] themes){
        List<Question> questionsListForTest = new ArrayList<Question>();
        List<Question> allQuestionsList = getAllQuestions();
        for (Question item:allQuestionsList) {
            for(int i = 0; i < themes.length; i++) {
                if (getTheme(item.getIdTheme()).getTitleTheme().equals(themes[i])) {
                    questionsListForTest.add(item);
                }
            }
        }
        return questionsListForTest;
    }


    @Override
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TEXT_QUESTION, question.getTextQuestion());
        contentValues.put(KEY_THEME_OF_QUESTION, question.getIdTheme());
        contentValues.put(KEY_WEIGHT_QUESTION, question.getWeight());
        return db.update(TABLE_QUESTION, contentValues, KEY_ID_QUESTION + " = ?", new String[] {String.valueOf(question.getNumber())});
    }


    @Override
    public void deleteQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTION, KEY_ID_QUESTION + " = ?", new String[] {String.valueOf(question.getNumber())});
        db.close();

        for (Answer answer:getAllAnswersForQuestion(question.getNumber())) {
            deleteAnswer(answer);
        }
    }

    public void deleteQuestionWithCheckTheme(Question question) {
        deleteQuestion(question);
        if (getQuestionForTheme(question.getIdTheme()).isEmpty()){
            deleteTheme(getTheme(question.getIdTheme()));
        }
    }

    @Override
    public void addTheme(Theme theme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE_THEME, theme.getTitleTheme());

        db.insert(TABLE_THEME, null, contentValues);
        db.close();
    }

    @Override
    public Theme getTheme(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_THEME, new String[] {
                        KEY_ID_THEME, KEY_TITLE_THEME},
                KEY_ID_THEME + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Theme theme = new Theme(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        return theme;
    }

    @Override
    public int getIdTheme(String theme){
        List<Theme> themeList = getAllThemes();
        int id = -1;
        for (Theme item:themeList) {
            if(item.getTitleTheme().equals(theme)){
                id = item.getIdTheme();
            }
        }
        return id;
    }

    @Override
    public List<Theme> getAllThemes() {
        List<Theme> themeList = new ArrayList<Theme>();
        String selectQuery = "SELECT * FROM " + TABLE_THEME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Theme theme = new Theme();
                theme.setIdTheme(Integer.parseInt(cursor.getString(0)));
                theme.setTitleTheme(cursor.getString(1));
                themeList.add(theme);
            } while (cursor.moveToNext());
        }
        return themeList;
    }

    @Override
    public String[] getArrThemes() {
        String[] themeArr = new String[getAllThemes().size()];
        int indexTextTheme = 0;
        for (Theme itemTheme : getAllThemes()) {
            themeArr[indexTextTheme] = itemTheme.getTitleTheme();
            indexTextTheme++;
        }
        return themeArr;
    }

    @Override
    public int updateTheme(Theme theme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE_THEME, theme.getTitleTheme());
        return db.update(TABLE_THEME, contentValues, KEY_ID_THEME + " = ?", new String[] {String.valueOf(theme.getIdTheme())});
    }

    @Override
    public int updateThemesInTest(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_THEMES, test.getThemes());
        return db.update(TABLE_TEST, contentValues, KEY_ID_TEST + " = ?", new String[] {String.valueOf(test.getIdTest())});
    }

    @Override
    public void deleteTheme(Theme theme) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_THEME, KEY_ID_THEME + " = ?", new String[] {String.valueOf(theme.getIdTheme())});
        db.close();

        for (Question question:getQuestionForTheme(theme.getIdTheme())){
            deleteQuestion(question);
        }

        for (Test test:getAllTests()) {
            String[] arrThemes = test.getArrThemes();
            for (int i = arrThemes.length - 1; i>=0; i--) {
                if(arrThemes[i].equals(theme.getTitleTheme())){
                    deleteThemeFromTest(test, arrThemes, theme.getTitleTheme());
                }
            }
        }
    }

    private void deleteThemeFromTest(Test test ,String[] arrThemes, String deletedTheme){
        String strThemes = "";
        for (int i = 0; i< arrThemes.length; i++) {
            if(!arrThemes[i].equals(deletedTheme)){
                strThemes += arrThemes[i] + "\n";
            }
        }
        if(arrThemes.length <= 1){
            deleteTest(test);
        }
        else {
            test.setThemes(strThemes);
            updateThemesInTest(test);
        }
    }

    @Override
    public void addAnswer (Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ANSWER, answer.getTextAnswer());
        contentValues.put(KEY_TRUE, answer.getValue());
        contentValues.put(KEY_NUMBER_QUESTION, answer.getNumberQuestion());

        db.insert(TABLE_ANSWER, null, contentValues);
        db.close();
    }

    @Override
    public Answer getAnswer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ANSWER, new String[] {
                        KEY_ID_ANSWER, KEY_ANSWER, KEY_TRUE, KEY_NUMBER_QUESTION},
                KEY_ID_ANSWER + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Answer answer = new Answer(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        return answer;
    }

    @Override
    public List<Answer> getAllAnswers() {
        List<Answer> answersList = new ArrayList<Answer>();
        String selectQuery = "SELECT * FROM " + TABLE_ANSWER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Answer answer = new Answer();
                answer.setIdAnswer(Integer.parseInt(cursor.getString(0)));
                answer.setTextAnswer(cursor.getString(1));
                answer.setValue(Integer.parseInt(cursor.getString(2)));
                answer.setNumberQuestion(Integer.parseInt(cursor.getString(3)));
                answersList.add(answer);
            } while (cursor.moveToNext());
        }
        return answersList;
    }

    @Override
    public String[] getArrAnswers() {
        String[] answersArray = new String[getAllAnswers().size()];
        int indexTextAnswer = 0;
        for (Answer itemAnswer : getAllAnswers()) {
            answersArray[indexTextAnswer] = itemAnswer.getTextAnswer();
            indexTextAnswer++;
        }
        return answersArray;
    }

    public String[] getArrAnswersForQuestion(Question question) {
        String[] answersArray = new String[getAllAnswersForQuestion(question.getNumber()).size()];
        int indexTextAnswer = 0;
        for (Answer itemAnswer : getAllAnswersForQuestion(question.getNumber())) {
            answersArray[indexTextAnswer] = itemAnswer.getTextAnswer();
            indexTextAnswer++;
        }
        return answersArray;
    }

    public List<Answer> getAllAnswersForQuestion(int idQuestion){
        List<Answer> answersListForQuestion = new ArrayList<Answer>();
        List<Answer> allAnswersList = getAllAnswers();
        for (Answer item:allAnswersList) {
            if(item.getNumberQuestion() == idQuestion){
                answersListForQuestion.add(item);
            }
        }
        return answersListForQuestion;
    }

    @Override
    public List<String> getAnswerForQuestion(int numberQuestion, int valueTrue){
        List<String> answersList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_ANSWER
                + " WHERE " + KEY_NUMBER_QUESTION + " = " + numberQuestion + " AND " + KEY_TRUE + " = " + valueTrue;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Answer answer = new Answer();
                answer.setIdAnswer(Integer.parseInt(cursor.getString(0)));
                answer.setTextAnswer(cursor.getString(1));
                answer.setValue(Integer.parseInt(cursor.getString(2)));
                answer.setNumberQuestion(Integer.parseInt(cursor.getString(3)));
                answersList.add(answer.getTextAnswer());
            } while (cursor.moveToNext());
        }
        return answersList;
    }


    @Override
    public int updateAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ANSWER, answer.getTextAnswer());
        contentValues.put(KEY_TRUE, answer.getValue());
        contentValues.put(KEY_NUMBER_QUESTION, answer.getNumberQuestion());
        return db.update(TABLE_ANSWER, contentValues, KEY_ID_ANSWER + " = ?", new String[] {String.valueOf(answer.getIdAnswer())});
    }

    @Override
    public void deleteAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANSWER, KEY_ID_ANSWER + " = ?", new String[] {String.valueOf(answer.getIdAnswer())});
        db.close();
    }

    @Override
    public void addTest(Test test){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TEST, test.getTitleTest());
        contentValues.put(KEY_MARK, test.getMaximalMark());
        contentValues.put(KEY_THEMES, test.getThemes());

        db.insert(TABLE_TEST, null, contentValues);
        db.close();
    }

    @Override
    public List<Test> getAllTests() {
        List<Test> testList = new ArrayList<Test>();
        String selectQuery = "SELECT * FROM " + TABLE_TEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Test test = new Test();
                test.setIdTest(Integer.parseInt(cursor.getString(0)));
                test.setTitleTest(cursor.getString(1));
                test.setMaximalMark(Float.parseFloat(cursor.getString(2)));
                test.setThemes(cursor.getString(3));
                testList.add(test);
            } while (cursor.moveToNext());
        }
        return testList;
    }

    @Override
    public String[] getArrTests() {
        String[] testArray = new String[getAllTests().size()];
        int indexTestArray = 0;
        for (Test item : getAllTests()) {
            testArray[indexTestArray] = item.getTitleTest();
            indexTestArray++;
        }
        return testArray;
    }

    @Override
    public int getIdTest(String test){
        List<Test> testList = getAllTests();
        int id = -1;
        for (Test item:testList) {
            if(item.getTitleTest().equals(test)){
                id = item.getIdTest();
            }
        }
        return id;
    }

    @Override
    public Test getTest(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TEST, new String[] {
                        KEY_ID_TEST, KEY_TEST, KEY_MARK, KEY_THEMES},
                KEY_ID_TEST + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Test test = new Test(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Float.parseFloat(cursor.getString(2)), cursor.getString(3));
        return test;
    }

    @Override
    public int updateTest(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TEST, test.getTitleTest());
        contentValues.put(KEY_MARK, test.getMaximalMark());
        contentValues.put(KEY_THEMES, test.getThemes());
        return db.update(TABLE_TEST, contentValues, KEY_ID_TEST + " = ?", new String[] {String.valueOf(test.getIdTest())});
    }

    @Override
    public void deleteTest(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEST, KEY_ID_TEST + " = ?", new String[] {String.valueOf(test.getIdTest())});
        db.close();

        for (Student student : getAllStudents()) {
            if(student.getIdTest() == test.getIdTest()){
                deleteStudent(student);
            }
        }
    }


    @Override
    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_STUDENT, student.getFullName());
        contentValues.put(KEY_ID_TEST_STUDENT, student.getIdTest());
        contentValues.put(KEY_MARK_STUDENT, student.getMark());
        contentValues.put(KEY_WRONG_QUESTIONS, student.getWrongQuestions());

        db.insert(TABLE_STUDENT, null, contentValues);
        db.close();
    }

    @Override
    public boolean updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_ID_TEST_STUDENT, student.getIdTest());
        args.put(KEY_MARK_STUDENT, student.getMark());
        args.put(KEY_WRONG_QUESTIONS, student.getWrongQuestions());
        return db.update(TABLE_STUDENT, args, KEY_ID_STUDENT + "=" + student.getIdStudent(), null) > 0;
    }

    @Override
    public int getLastIdStudent() {
        int lastId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID_STUDENT + " from " + TABLE_STUDENT + " order by " + KEY_ID_STUDENT + " DESC limit 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            lastId = cursor.getInt(0);
        }
        return lastId;
    }

    @Override
    public Student getStudent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STUDENT, new String[] {
                        KEY_ID_STUDENT, KEY_NAME_STUDENT, KEY_ID_TEST_STUDENT, KEY_MARK_STUDENT, KEY_WRONG_QUESTIONS},
                KEY_ID_STUDENT + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Student student = new Student(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Float.parseFloat(cursor.getString(3)), cursor.getString(4));
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<Student>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Student student = new Student();
                student.setIdStudent(Integer.parseInt(cursor.getString(0)));
                student.setFullName(cursor.getString(1));
                student.setIdTest(Integer.parseInt(cursor.getString(2)));
                student.setMark(Float.parseFloat(cursor.getString(3)));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        return studentList;
    }


    @Override
    public int getIdStudent(Student student){
        List<Student> studentList = getAllStudents();
        int id = -1;

        for (Student item:studentList) {
            if(item.getFullName().equals(student.getFullName())
                    && item.getIdTest() == student.getIdTest()
                    && item.getMark() == student.getMark()){
                id = item.getIdStudent();
            }
        }
        return id;
    }

    @Override
    public String[] getArrStudents() {
        String[] studentsArray = new String[getAllStudents().size()];
        int indexStudentArray = 0;
        for (Student item : getAllStudents()) {
            studentsArray[indexStudentArray] = item.getFullName();
            indexStudentArray++;
        }
        return studentsArray;
    }

    @Override
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENT, KEY_ID_STUDENT + " = ?", new String[] {String.valueOf(student.getIdStudent())});
        db.close();
    }

    @Override
    public void addUser(User user) {
        Log.i("addToDb", user.getLogin() + "");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LOGIN_USER, user.getLogin());
        contentValues.put(KEY_NAME_USER, user.getFullName());
        contentValues.put(KEY_PASS_USER, user.getPass());
        contentValues.put(KEY_RANK, user.getIsAdmin());

        db.insert(TABLE_USER, null, contentValues);
        db.close();
    }

    @Override
    public int getIdUser(String login){
        List<User> userList = getAllUsers();
        int id = -1;
        for (User item:userList) {
            if(item.getLogin().equals(login)){
                id = item.getIdUser();
            }
        }
        return id;
    }

    @Override
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] {
                        KEY_ID_USER, KEY_LOGIN_USER, KEY_NAME_USER, KEY_PASS_USER, KEY_RANK},
                KEY_ID_USER + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        return user;
    }



    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                User user = new User();
                user.setIdUser(Integer.parseInt(cursor.getString(0)));
                user.setLogin(cursor.getString(1));
                user.setFullName(cursor.getString(2));
                user.setPass(cursor.getString(3));
                user.setIsAdmin(Integer.parseInt(cursor.getString(4)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public List<User> getUsersForRank(int rank) {
        List<User> userList = getAllUsers();
        List<User> userListRank = new ArrayList<>();
        if(!userList.isEmpty()) {
            for (User user : userList) {
                if (user.getIsAdmin() == rank) {
                    userListRank.add(user);
                }
            }
        }
        return userListRank;
    }

        @Override
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID_USER + " = ?", new String[] {String.valueOf(user.getIdUser())});
        db.close();
    }

    @Override
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LOGIN_USER, user.getLogin());
        contentValues.put(KEY_NAME_USER, user.getFullName());
        contentValues.put(KEY_PASS_USER, user.getPass());
        return db.update(TABLE_USER, contentValues, KEY_ID_USER + " = ?", new String[] {String.valueOf(user.getIdUser())});
    }



}
