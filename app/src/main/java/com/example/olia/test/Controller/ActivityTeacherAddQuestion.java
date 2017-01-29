package com.example.olia.test.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.Entity.Theme;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ActivityTeacherAddQuestion extends Fragment implements AdapterView.OnItemSelectedListener {
    public ActivityTeacherAddQuestion() {
    }

    private Button buttonAddQuestion, buttonAddTheme;
    private EditText editTextQuestion, editTextWeight, editTextTrueAnswer, editTextFalseAnswer, editTextTheme;
    private Spinner themeSpinner;
    private DatabaseHandler database;
    private Helper helper;
    private ArrayAdapter<String> adapter;
    private List<String> listThemes;

    private String questionText, weight, trueAnswer, falseAnswer, theme;
    private int iNumberQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.screen_add_question, container, false);

        buttonAddQuestion = (Button) rootView.findViewById(R.id.buttonAddInAddQ);
        editTextQuestion = (EditText) rootView.findViewById(R.id.editTextQuestionInAddQ);
        editTextWeight = (EditText) rootView.findViewById(R.id.editTextWeightInAddQ);
        editTextTrueAnswer = (EditText) rootView.findViewById(R.id.editTextTrueAnswerInAddQ);
        editTextFalseAnswer = (EditText) rootView.findViewById(R.id.editTextFalseAnswerInAddQ);
        themeSpinner = (Spinner) rootView.findViewById(R.id.spinnerThemeInAddQ);

        helper = new Helper();
        database = new DatabaseHandler(this.getContext());

        editTextQuestion.addTextChangedListener(mainTextWatcher);
        editTextWeight.addTextChangedListener(mainTextWatcher);
        editTextTrueAnswer.addTextChangedListener(mainTextWatcher);
        editTextFalseAnswer.addTextChangedListener(mainTextWatcher);
        checkMainFields();

        setThemeSpinner();
        buttonAddQuestion.setOnClickListener(clickListener);

        return rootView;
    }

    private TextWatcher mainTextWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkMainFields();
        }
    });

    private View.OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v) {
            if(isAddToDB()) {
                Intent intent = new Intent(v.getContext(), ActivityTeacherNewQuestion.class);
                intent.putExtra(getResources().getString(R.string.numberQ),
                        ((Integer) iNumberQuestion).toString());
                startActivity(intent);
            }
        }
    };

    private void setThemeSpinner(){
        listThemes = new ArrayList<>(Arrays.asList(database.getArrThemes()));
        listThemes.add(getResources().getString(R.string.add_new_theme));

        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listThemes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (listThemes.get(position).equals(getResources().getString(R.string.add_new_theme))){
            themeSpinner.setSelection(0);
            createDialog(position);
        }
        theme = listThemes.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createDialog(int position){
        final int pos = position;
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.toast_add_theme);
        dialog.setTitle(getResources().getString(R.string.add_new_theme));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        dialog.getWindow().setAttributes(lp);

        buttonAddTheme = (Button) dialog.findViewById(R.id.buttonAddTheme);
        editTextTheme = (EditText) dialog.findViewById(R.id.editTextThemeInD);
        editTextTheme.addTextChangedListener(dialogTextWatcher);
        checkFieldTheme();
        buttonAddTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listThemes.contains(theme)) {
                    listThemes.add(pos, theme);
                    adapter.notifyDataSetChanged();
                    themeSpinner.setSelection(pos);
                }
                else {
                    helper.makeToastWithId(R.string.message_theme_exists, getView());
                    themeSpinner.setSelection(listThemes.indexOf(theme));
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private TextWatcher dialogTextWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldTheme();
        }
    });

    private void checkMainFields(){
        questionText = helper.checkInput(editTextQuestion);
        weight = helper.checkInput(editTextWeight);
        trueAnswer = helper.checkInput(editTextTrueAnswer);
        falseAnswer = helper.checkInput(editTextFalseAnswer);

        buttonAddQuestion.setEnabled(isCorrectValue());
    }

    private boolean isCorrectValue(){
        return !(questionText.isEmpty() || weight.isEmpty() || trueAnswer.isEmpty() || falseAnswer.isEmpty()
                || theme.equals(getResources().getString(R.string.add_new_theme)));
    }

    private void checkFieldTheme(){
        theme = helper.checkInput(editTextTheme);
        buttonAddTheme.setEnabled(!theme.isEmpty());
    }

    private boolean isAddToDB() {
        if (!isContainQuestion()) {
            return checkEqualAnswer();
        }
        else {
            helper.makeToastWithId(R.string.message_question_exists, getView());
            return false;
        }
    }

    private boolean isContainQuestion(){
        boolean isContain = false;
        for (Question item : database.getAllQuestions()) {
            if (item.getTextQuestion().equals(questionText)
                    && database.getTheme(item.getIdTheme()).getTitleTheme().equals(theme)) {
                isContain = true;
            }
        }
        return isContain;
    }

    private boolean checkEqualAnswer(){
        if(!trueAnswer.equals(falseAnswer)) {
            List<String> newListTheme = new ArrayList<>(Arrays.asList(database.getArrThemes()));
            if (!newListTheme.contains(theme)) {
                database.addTheme(new Theme(theme));
            }
            int idTheme = database.getIdTheme(theme);
            Question questionToAdd = new Question(questionText, idTheme, Float.parseFloat(weight));
            database.addQuestion(questionToAdd);

            iNumberQuestion = database.getIdQuestion(questionToAdd);
            database.addAnswer(new Answer(trueAnswer, 0, iNumberQuestion));
            database.addAnswer(new Answer(falseAnswer, 1, iNumberQuestion));
            return true;
        }
        else {
            helper.makeToastWithId(R.string.message_equal_answer, getView());
            return false;
        }
    }

}
