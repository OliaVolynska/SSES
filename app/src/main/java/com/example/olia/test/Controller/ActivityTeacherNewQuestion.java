package com.example.olia.test.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.R;

import java.util.List;


public class ActivityTeacherNewQuestion extends Activity implements View.OnClickListener {
    private Button buttonAddTrue, buttonAddFalse;
    private DatabaseHandler database;
    private Helper helper;
    private int numberQuestion;

    private EditText editTextTrueAnswer;
    private EditText editTextFalseAnswer;
    private TextView textViewTrue, textViewFalse;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_about_new_question);

        database = new DatabaseHandler(getBaseContext());
        helper = new Helper();

        TextView textViewQuestion = (TextView) findViewById (R.id.textViewInfQuestion);
        TextView textViewTheme = (TextView) findViewById (R.id.textViewInfTheme);
        TextView textViewWeight = (TextView) findViewById (R.id.textViewInfWeight);
        textViewTrue = (TextView) findViewById (R.id.textViewInfTrue);
        textViewFalse = (TextView) findViewById (R.id.textViewInfFalse);

        numberQuestion = Integer.parseInt(getIntent().getStringExtra(getResources().getString(R.string.numberQ)));
        Question question = database.getQuestion(numberQuestion);

        helper.setTextView(textViewQuestion, question.getTextQuestion());
        helper.setTextView(textViewTheme, database.getTheme(question.getIdTheme()).getTitleTheme());
        helper.setTextView(textViewWeight, ((Float)question.getWeight()).toString());
        helper.setTextView(textViewTrue, getStrAnswers(helper.VALUE_TRUE));
        helper.setTextView(textViewFalse, getStrAnswers(helper.VALUE_FALSE));

        Button buttonAddAnswers = (Button) findViewById(R.id.buttonAddAns);
        Button buttonBack = (Button) findViewById(R.id.buttonBackToMenuTeacher);
        buttonAddAnswers.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAddAns:
                createDialog();
                break;
            case R.id.buttonBackToMenuTeacher:
                Intent intent = new Intent(v.getContext(), ActivityTeacherMain.class);
                startActivity(intent);
                break;
            case R.id.buttonAddTrue:
                addAnswer(editTextTrueAnswer, textViewTrue, R.string.true_answer, helper.VALUE_TRUE);
                break;
            case R.id.buttonAddFalse:
                addAnswer(editTextFalseAnswer, textViewFalse, R.string.false_answer, helper.VALUE_FALSE);
                break;
        }
    }

    private void addAnswer(EditText editText, TextView textView, int idStr, int value){
        if(!isExistAnswer(editText)) {
            database.addAnswer(new Answer(helper.checkInput(editText), value, numberQuestion));
            textView.setText(getResources().getString(idStr) + getStrAnswers(value));
            editText.setText("");
        }
        else{
            helper.makeToastWithId(R.string.message_answer_exists,
                    this.getWindow().getDecorView().getRootView());
        }
    }

    private void createDialog(){
        final Dialog dialog = new Dialog(ActivityTeacherNewQuestion.this);
        dialog.setContentView(R.layout.toast_add_answers);
        dialog.setTitle(getResources().getString(R.string.add_answers));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        dialog.getWindow().setAttributes(lp);

        editTextTrueAnswer = (EditText) dialog.findViewById(R.id.editTextTrueAnswerInD);
        editTextFalseAnswer = (EditText) dialog.findViewById(R.id.editTextFalseAnswerInD);
        buttonAddTrue = (Button) dialog.findViewById(R.id.buttonAddTrue);
        buttonAddFalse = (Button) dialog.findViewById(R.id.buttonAddFalse);

        editTextTrueAnswer.addTextChangedListener(textWatcher);
        editTextFalseAnswer.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();

        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancelAddAnsw);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonAddTrue.setOnClickListener(this);
        buttonAddFalse.setOnClickListener(this);

        dialog.show();
    }

    private TextWatcher textWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForEmptyValues();
        }
    });

    private String getStrAnswers(int value){
        StringBuilder str = new StringBuilder();
        List<String> listAnswer = database.getAnswerForQuestion(numberQuestion, value);
        String lastAnswer = listAnswer.get(listAnswer.size() - 1);
        for(String item : listAnswer){
            str.append(" ");
            str.append(item);
            if(!item.equals(lastAnswer)) {
                str.append(";");
            }
            else {
                str.append(".");            }
        }
        return str.toString();
    }

    private boolean isExistAnswer(EditText editText){
        boolean isExist = false;
        for(int value = helper.VALUE_TRUE; value <= helper.VALUE_FALSE; value++)
        if(database.getAnswerForQuestion(numberQuestion, value).contains(helper.checkInput(editText))){
            isExist = true;
        }
        return isExist;
    }

    private void checkFieldsForEmptyValues(){
        buttonAddTrue.setEnabled(!helper.checkInput(editTextTrueAnswer).isEmpty());
        buttonAddFalse.setEnabled(!helper.checkInput(editTextFalseAnswer).isEmpty());
    }
}