package com.example.olia.test.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.Entity.Student;
import com.example.olia.test.Entity.Test;
import com.example.olia.test.Entity.User;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ActivityStudentTest extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    private TextView questionTextView;
    private ImageButton nextButton, prevButton;
    private Button confirmButton;
    private ListView listRadioButton;

    private DatabaseHandler database;
    private Helper helper;
    private Student student;
    private Test test;

    private List<String> listTextAnswer, selected, unSelected;
    private List<Answer> answersList;
    private List<Question> questions;
    private int currentIndexQuestions = 0;
    private String wrongQuestions = "";
    private float maximalResult;

    private final int TIME_DELAYED = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        database = new DatabaseHandler(this);
        helper = new Helper();
        student = new Student();

        questionTextView = (TextView)findViewById(R.id.questionTextView);
        nextButton = (ImageButton) findViewById(R.id.buttonNext);
        prevButton = (ImageButton)findViewById(R.id.buttonPrev);
        confirmButton = (Button) findViewById(R.id.buttonConfirm);
        listRadioButton = (ListView) findViewById(R.id.listRadioButton);

        addNewStudent();
        questions = database.getQuestionForTest(test.getArrThemes());
        maximalResult = getMaximalResult();
        updateQuestion();

        if(questions.size() == 1){
            setVisibilityButtons(View.INVISIBLE);
        }
        confirmButton.setEnabled(false);

        listRadioButton.setOnItemClickListener(this);
        confirmButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int countChoice = listRadioButton.getCount();
        selected = new ArrayList<>();
        unSelected = new ArrayList<>();

        SparseBooleanArray sparseBooleanArray = listRadioButton.getCheckedItemPositions();
        for (int i = 0; i < countChoice; i++){
            if(sparseBooleanArray.get(i)){
                selected.add(listRadioButton.getItemAtPosition(i).toString());
            }
            else if(!sparseBooleanArray.get(i)){
                unSelected.add(listRadioButton.getItemAtPosition(i).toString());
            }
        }
        if(selected.isEmpty()){
            confirmButton.setEnabled(false);
        }
        else confirmButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonConfirm:
                confirm();
                break;

            case R.id.buttonNext:
                currentIndexQuestions = (currentIndexQuestions + 1) % questions.size();
                updateQuestion();
                break;

            case R.id.buttonPrev:
                if(currentIndexQuestions > 0) {
                    currentIndexQuestions = (currentIndexQuestions - 1) % questions.size();
                } else
                if(currentIndexQuestions == 0){
                    currentIndexQuestions = questions.size() - 1;
                }
                updateQuestion();
                break;
        }
    }

    private void addNewStudent(){
        String login = getIntent().getStringExtra(getResources().getString(R.string.login));
        String titleTest = getIntent().getStringExtra(getResources().getString(R.string.test_for_extra));

        int idUser = database.getIdUser(login);
        User user = database.getUser(idUser);
        String fullName = user.getFullName();

        TextView studentNameTextView = (TextView) findViewById(R.id.textViewStudentName);
        TextView testTextView = (TextView) findViewById(R.id.textViewThemeTest);
        helper.setTextView(studentNameTextView, fullName);
        helper.setTextView(testTextView, titleTest);

        int idTest = database.getIdTest(titleTest);
        student = new Student(fullName, idTest, 0.0f,
                getResources().getString(R.string.test_not_finished));
        test = database.getTest(idTest);

        database.addStudent(student);
    }

    private void confirm(){
        if(questions.size()!= 0) {
            confirmButton.setEnabled(false);
            setEnableButtons(false);
            checkWrongQuestion();

            questions.remove(currentIndexQuestions);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    actionAfterDelayed();
                }
            }, TIME_DELAYED);
        }
    }

    private void actionAfterDelayed(){
        if((currentIndexQuestions == questions.size() && (questions.size() != 0))){
            currentIndexQuestions--;
            updateQuestion();
        }
        else if(questions.size() == 0){
            updateLastStudent();
            final Intent intent = new Intent(getBaseContext(), ActivityStudentResult.class);
            startActivity(intent);
        }
        else {
            updateQuestion();
        }
        if(questions.size() == 1){
            setVisibilityButtons(View.INVISIBLE);
        }
        setEnableButtons(true);
    }

    private void setEnableButtons(Boolean value){
        prevButton.setEnabled(value);
        nextButton.setEnabled(value);
    }

    private void setVisibilityButtons(int value){
        prevButton.setVisibility(value);
        nextButton.setVisibility(value);
    }

    private void updateLastStudent(){
        if(wrongQuestions.isEmpty()){
            student.setWrongQuestions(getResources().getString(R.string.perfectly));
        }
        else {
            student.setWrongQuestions(wrongQuestions);
        }
        student.setIdStudent(database.getLastIdStudent());
        student.setMark(getStudentMark());
        database.updateStudent(student);
    }

    private void updateQuestion(){
        questionTextView.setText(questions.get(currentIndexQuestions).getTextQuestion());
        updateAnswer();
    }

    private void updateAnswer(){
        answersList = database.getAllAnswersForQuestion(
                questions.get(currentIndexQuestions).getNumber());
        long seed = System.nanoTime();
        Collections.shuffle(answersList, new Random(seed));

        String[] answers = new String[answersList.size()];
        int indexArr = 0;
        for (Answer item:answersList) {
            answers [indexArr] = item.getTextAnswer();
            indexArr++;
        }
        listTextAnswer = new ArrayList<>(Arrays.asList(answers));
        listRadioButton.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_multiple_choice, listTextAnswer);
        listRadioButton.setAdapter(adapter);
    }

    private void checkWrongQuestion(){
        List<String> listFalse = new ArrayList<>();
        List<String> listTrue = new ArrayList<>();
        String check = checkSelected(listFalse, listTrue);

        if(check.equals(getResources().getString(R.string.incorrect))){
            wrongQuestions += questions.get(currentIndexQuestions).getTextQuestion() + "\n";
        }
        else {
            student.setResult(questions.get(currentIndexQuestions).getWeight());
        }
        helper.makeToastWithStr(check, getCurrentFocus());
        setHighlight(listTrue, listFalse);
        setSelectedItem();
    }

    private String checkSelected(List<String> listFalse, List<String> listTrue){
        String check = getResources().getString(R.string.correct);
        for(Answer answer:answersList) {
            if (answer.getValue() == helper.VALUE_FALSE) {
                for (String choose: selected) {
                    if(answer.getTextAnswer().equals(choose)){
                        check = getResources().getString(R.string.incorrect);
                        listFalse.add(choose);
                    }
                }
            }
            else {
                listTrue.add(answer.getTextAnswer());
                for(String unCheck: unSelected){
                    if(answer.getTextAnswer().equals(unCheck)){
                        check = getResources().getString(R.string.incorrect);
                    }
                }
            }
        }
        return check;
    }

    private void setHighlight(final List<String> listTruePosition,
                              final List<String> listFalsePosition){
        final List<String> listTrueUnChecked = findTrueUnchecked();
        listRadioButton.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, listTextAnswer)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                final View renderer = super.getView(position, convertView, parent);
                List <Integer> listPosition = new ArrayList<>();
                highlightForPosition(renderer, android.R.color.holo_green_dark,
                        listTruePosition, position, listPosition);
                highlightForPosition(renderer, android.R.color.holo_red_dark,
                        listFalsePosition, position, listPosition);
                highlightForPosition(renderer, android.R.color.holo_orange_light,
                        listTrueUnChecked, position, listPosition);

                return renderer;
            }
        });
    }

    private List<String> findTrueUnchecked(){
        final List<String> listTrueUnChecked = new ArrayList<>();
        for(Answer answer:answersList) {
            for (String unCheck: unSelected){
                if(answer.getTextAnswer().equals(unCheck) && answer.getValue() == helper.VALUE_TRUE){
                    listTrueUnChecked.add(unCheck);
                }
            }
        }
        return listTrueUnChecked;
    }

    private void highlightForPosition(final View renderer, int color, List <String> listForHighlight,
                                      int position, List <Integer> listPosition){
        for(int i = listForHighlight.size() -1; i>=0; i--){
            if (answersList.get(position).getTextAnswer().equals(listForHighlight.get(i))) {
                renderer.setBackgroundResource(color);
                listPosition.add(position);
            }
            if(!listPosition.contains(position)){
                renderer.setBackgroundResource(android.R.color.transparent);
            }
        }
    }

    private void setSelectedItem(){
        int index = 0;
        for(Answer answer:answersList) {
            for (String choose: selected) {
                if(answer.getTextAnswer().equals(choose)){
                    listRadioButton.setItemChecked(index, true);
                }
            }
            index++;
        }
    }

    private float getMaximalResult(){
        float maximalResult = 0.0f;
        for (Question question:questions) {
            maximalResult += question.getWeight();
        }
        return maximalResult;
    }

    private float getStudentMark(){
        float resultStudent = student.getResult();
        float maximalMark = test.getMaximalMark();
        return (resultStudent*maximalMark)/maximalResult;
    }
}
