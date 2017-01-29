package com.example.olia.test.Controller;


import android.content.DialogInterface;
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
import android.widget.ListView;

import com.example.olia.test.Entity.NewsItem;
import com.example.olia.test.Entity.Student;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityTeacherShowStudents extends Fragment {
    private DatabaseHandler database;
    private ArrayList arrayList;
    private View rootView;

    public ActivityTeacherShowStudents() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.screeen_show_students, container, false);
        updateAdapter();

        return rootView;
    }

    private ArrayList getListData() {
        database = new DatabaseHandler(this.getContext());
        ArrayList<NewsItem> newsItemResults = new ArrayList<NewsItem>();
        List<Student> studentList = database.getAllStudents();

        if (studentList.size() != 0) {
            for (Student student : studentList) {
                NewsItem newsData = new NewsItem();
                newsData.setHeadline(student.getFullName());
                newsData.setSubItem(database.getTest(student.getIdTest()).getTitleTest());
                newsData.setAnotherItem(((Float) student.getMark()).toString());
                newsItemResults.add(newsData);
            }
        }
        return newsItemResults;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_students, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.show:
                showWrongQuestions(info.position);
                return true;
            case R.id.delete:
                deleteItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private int getIdStudentInPosition(int position){
        NewsItem newsItem = (NewsItem) arrayList.get(position);
        String  fullName = newsItem.getHeadline();
        int idTest = database.getIdTest(newsItem.getSubItem());
        Float mark = Float.parseFloat(newsItem.getAnotherItem());
        Student student = new Student(fullName, idTest, mark, "");

        return database.getIdStudent(student);
    }

    private void deleteItem(int position){
        database.deleteStudent(database.getStudent(getIdStudentInPosition(position)));
        updateAdapter();
    }

    private void showWrongQuestions(int position){
        Student student = database.getStudent(getIdStudentInPosition(position));
        createWrongQAlertDialog(student);
    }

    private void updateAdapter(){
        arrayList = getListData();
        final ListView listView = (ListView) rootView.findViewById(R.id.listViewStudents);

        CustomListAdapter adapter = new CustomListAdapter(getContext(), arrayList);
        listView.setAdapter(adapter);
        ColorDrawable dividerColor = new ColorDrawable(
                this.getResources().getColor(R.color.green));
        listView.setDivider(dividerColor);
        listView.setDividerHeight(2);
        registerForContextMenu(listView);
    }

    private void createWrongQAlertDialog(Student student) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.wrong_questions));
        String[] questions = student.getArrQuestions();

        builder.setItems(questions , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
            }
        });
        builder.show();
    }
}

