package com.example.olia.test.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityTeacherEditQuestion extends Activity implements View.OnClickListener {
    private EditText editTextQuestion, editTextWeight, editTextTrueAnswer, editTextFalseAnswer;
    private Button buttonEdit, buttonAddTrue, buttonAddFalse;
    private Question question;
    private DatabaseHandler database;
    private Helper helper;

    private int idQuestion;
    private List<Answer> answerList;
    private List<String> trueList, falseList;
    private ArrayAdapter<String> adapterTrue, adapterFalse;
    private boolean isListTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_question);

        buttonEdit = (Button) findViewById(R.id.buttonEditInEditQ);
        editTextQuestion = (EditText) findViewById(R.id.editTextQuestionInEditQ);
        editTextWeight = (EditText) findViewById(R.id.editTextWeightInEditQ);

        helper = new Helper();
        database = new DatabaseHandler(this);
        idQuestion = Integer.parseInt(getIntent().getStringExtra(
                getResources().getString(R.string.numberQ)));
        question = database.getQuestion(idQuestion);

        editTextQuestion.setText(question.getTextQuestion());
        editTextWeight.setText(((Float)question.getWeight()).toString());
        editTextQuestion.addTextChangedListener(mainTextWatcher);
        editTextWeight.addTextChangedListener(mainTextWatcher);
        checkMainFieldsForEmptyValues();

        Button buttonAddAnswer = (Button) findViewById(R.id.buttonAddNewAnsw);
        buttonAddAnswer.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);

        makeListViews();
    }

    private TextWatcher mainTextWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkMainFieldsForEmptyValues();
        }
    });

    private void makeListViews(){
        ListView listViewTrue = (ListView) findViewById(R.id.listTrueAnswers);
        ListView listViewFalse = (ListView) findViewById(R.id.listFalseAnswers);

        answerList = database.getAllAnswersForQuestion(idQuestion);
        trueList = new ArrayList<>();
        falseList = new ArrayList<>();
        for (Answer answer:answerList) {
            if(answer.getValue() == helper.VALUE_TRUE){
                trueList.add(answer.getTextAnswer());
            }
            else {
                falseList.add(answer.getTextAnswer());
            }
        }
        adapterTrue = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1, trueList);
        adapterFalse = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1, falseList);
        setAdapters(listViewTrue, adapterTrue);
        setAdapters(listViewFalse, adapterFalse);
    }

    private void setAdapters(ListView listView, ArrayAdapter<String> adapter){
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.listTrueAnswers){
            isListTrue = true;
        }
        else if(v.getId() == R.id.listFalseAnswers){
            isListTrue = false;
        }
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_menu_answers, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_answer:
                delete(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void delete(int position){
        if(isListTrue){
            deleteAnswer(position, trueList, adapterTrue, R.string.message_delete_last_true);
        }
        else {
            deleteAnswer(position, falseList, adapterFalse, R.string.message_delete_last_false);
        }
    }

    private void deleteAnswer(int position, List<String> list,
                              ArrayAdapter<String> adapter, int idStr){
        if(list.size() != 1) {
            for (Answer answer : answerList) {
                if (answer.getTextAnswer().equals(list.get(position))) {
                    database.deleteAnswer(answer);
                }
            }
            adapter.notifyDataSetChanged();
            adapter.remove(list.get(position));
        }
        else {
            helper.makeToastWithId(idStr, getCurrentFocus());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAddNewAnsw:
                createDialogAddAnswers();
                break;
            case R.id.buttonEditInEditQ:
                edit();
                break;
            case R.id.buttonAddTrue:
                addAnswer(editTextTrueAnswer, adapterTrue, helper.VALUE_TRUE);
                break;
            case R.id.buttonAddFalse:
                addAnswer(editTextFalseAnswer, adapterFalse, helper.VALUE_FALSE);
                break;
        }
    }

    private void createDialogAddAnswers(){
        final Dialog dialog = new Dialog(ActivityTeacherEditQuestion.this);
        dialog.setContentView(R.layout.toast_add_answers);
        dialog.setTitle(getResources().getString(R.string.add_answers));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        dialog.getWindow().setAttributes(lp);

        editTextTrueAnswer = (EditText) dialog.findViewById(R.id.editTextTrueAnswerInD);
        editTextFalseAnswer = (EditText) dialog.findViewById(R.id.editTextFalseAnswerInD);
        buttonAddTrue = (Button) dialog.findViewById(R.id.buttonAddTrue);
        buttonAddFalse = (Button) dialog.findViewById(R.id.buttonAddFalse);

        editTextTrueAnswer.addTextChangedListener(dialogTextWatcher);
        editTextFalseAnswer.addTextChangedListener(dialogTextWatcher);
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

    private TextWatcher dialogTextWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForEmptyValues();
        }
    });


    private void addAnswer(EditText editText, ArrayAdapter<String> adapter, int value){
        if(!isExistAnswer(editText)) {
            String answer = helper.checkInput(editText);
            database.addAnswer(new Answer(answer, value, idQuestion));
            editText.setText("");
            adapter.notifyDataSetChanged();
            adapter.add(answer);
        }
        else{
            helper.makeToastWithId(R.string.message_answer_exists, getCurrentFocus());
        }
    }

    private void edit(){
        if(!isContainQuestion()) {
            question.setTextQuestion(helper.checkInput(editTextQuestion));
            question.setWeight(Float.parseFloat(helper.checkInput(editTextWeight)));
            database.updateQuestion(question);
            helper.createDialogAfterEdit(ActivityTeacherEditQuestion.this,
                    R.string.edit, R.string.message_question_edited);
        }
        else {
            helper.makeToastWithId(R.string.message_question_exists, getCurrentFocus());
        }
    }

    private boolean isContainQuestion(){
        boolean isContain = false;
        for (Question questionForTheme:database.getQuestionForTheme(question.getIdTheme())) {
            if(questionForTheme.getTextQuestion().equals(helper.checkInput(editTextQuestion))
                    && !questionForTheme.getTextQuestion().equals(question.getTextQuestion())){
                isContain = true;
            }
        }
        return isContain;
    }

    private boolean isExistAnswer(EditText editText){
        boolean isExist = false;
        for(int value = helper.VALUE_TRUE; value<=helper.VALUE_FALSE; value++)
            if(database.getAnswerForQuestion(idQuestion, value).contains(helper.checkInput(editText))){
                isExist = true;
            }
        return isExist;
    }

    private void checkMainFieldsForEmptyValues(){
        buttonEdit.setEnabled(isCorrectValueEdit());
    }

    private boolean isCorrectValueEdit(){
        return !(helper.checkInput(editTextQuestion).isEmpty() || helper.checkInput(editTextWeight).isEmpty()
                || (helper.checkInput(editTextQuestion).equals(question.getTextQuestion()) &&
                Float.parseFloat(helper.checkInput(editTextWeight)) == question.getWeight()));
    }

    private void checkFieldsForEmptyValues(){
        buttonAddTrue.setEnabled(!helper.checkInput(editTextTrueAnswer).isEmpty());
        buttonAddFalse.setEnabled(!helper.checkInput(editTextFalseAnswer).isEmpty());
    }
}
