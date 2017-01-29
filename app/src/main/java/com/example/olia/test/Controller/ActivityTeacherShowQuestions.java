package com.example.olia.test.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.olia.test.Entity.Answer;
import com.example.olia.test.Entity.NewsItem;
import com.example.olia.test.Entity.Question;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityTeacherShowQuestions extends Fragment {
    private DatabaseHandler database;
    private ArrayList arrayList;
    private View rootView;

    public ActivityTeacherShowQuestions(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.screen_show_all_questions, container, false);

        Button buttonAddQuestion = (Button) rootView.findViewById(R.id.buttonAddQuestionInList);
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityTeacherMain.class);
                startActivity(intent);
            }
        });
        updateAdapter();

        return rootView;
    }

    private ArrayList getListData() {
        database = new DatabaseHandler(this.getContext());
        ArrayList<NewsItem> newsItemQuestions = new ArrayList<>();
        List<Question> questions = database.getAllQuestions();
        if (questions.size() != 0) {
            for (Question question : questions) {
                NewsItem newsData = new NewsItem();
                newsData.setHeadline(question.getTextQuestion());
                newsData.setSubItem(database.getTheme(question.getIdTheme()).getTitleTheme());
                newsData.setAnotherItem(((Float) question.getWeight()).toString());
                newsItemQuestions.add(newsData);
            }
        }
        return newsItemQuestions;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_questions, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.show_answers:
                showAnswers(info.position);
                return true;
            case R.id.edit_question:
                editQuestion(info.position);
                return true;
            case R.id.delete_question:
                deleteItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private int getIdQuestionInPos(int position){
        NewsItem newsItem = (NewsItem) arrayList.get(position);
        int idQuestion = 0;
        for (Question question:database.getAllQuestions()) {
            if(question.getTextQuestion().equals(newsItem.getHeadline())
                    && database.getTheme(question.getIdTheme()).getTitleTheme().equals(newsItem.getSubItem())){
                idQuestion = question.getNumber();
            }
        }
        return idQuestion;
    }

    private void showAnswers(int position){
        Question chooseQuestion = database.getQuestion(getIdQuestionInPos(position));
        createAnswersAlertDialog(chooseQuestion);
    }

    private void deleteItem(int position){
        Question questionToDelete = database.getQuestion(getIdQuestionInPos(position));
        database.deleteQuestionWithCheckTheme(questionToDelete);
        updateAdapter();
    }

    private void editQuestion(int position){

        Intent intent = new Intent(getContext(), ActivityTeacherEditQuestion.class);
        intent.putExtra(getResources().getString(R.string.numberQ), ((Integer) getIdQuestionInPos(position)).toString());
        startActivity(intent);
    }

    private void updateAdapter(){
        arrayList = getListData();
        final ListView listView = (ListView) rootView.findViewById(R.id.listViewQuestions);

        listView.setAdapter(new CustomListAdapter(getContext(), arrayList));
        ColorDrawable dividerColor = new ColorDrawable(
                this.getResources().getColor(R.color.green));
        listView.setDivider(dividerColor);
        listView.setDividerHeight(2);
        registerForContextMenu(listView);
    }

    private void createAnswersAlertDialog(Question question) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.show_answers));
        final String[] answers = database.getArrAnswersForQuestion(question);
        final List<Answer> answerList = database.getAllAnswersForQuestion(question.getNumber());
        builder.setItems(answers , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                builder.show();
                Helper helper = new Helper();
                String message = getResources().getString(R.string.incorrect);
                if(answerList.get(item).getValue() == helper.VALUE_TRUE){
                    message = getResources().getString(R.string.correct);
                }
                helper.makeToastWithStr(message, rootView);
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}

