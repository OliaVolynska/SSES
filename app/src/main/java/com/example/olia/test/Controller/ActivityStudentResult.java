package com.example.olia.test.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.olia.test.Entity.Student;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityStudentResult extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_result_activity);

        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        TextView textViewTest = (TextView) findViewById(R.id.textViewTest);
        TextView textViewMark = (TextView) findViewById(R.id.textViewResult);

        Helper helper = new Helper();
        DatabaseHandler database = new DatabaseHandler(this);
        Student student = database.getStudent(database.getLastIdStudent());
        String titleTest = database.getTest(student.getIdTest()).getTitleTest();

        helper.setTextView(textViewName, student.getFullName());
        helper.setTextView(textViewTest, titleTest);
        helper.setTextView(textViewMark, ((Float)student.getMark()).toString());

        setListQuestions(student);

        Button back = (Button) findViewById(R.id.buttonBackInRes);
        back.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), ActivityFirstTab.class);
            startActivity(intent);
        }
    };

    private void setListQuestions(Student student){
        ListView listViewQuestions = (ListView) findViewById(R.id.listViewWrongQuestion);
        List <String> listTextAnswer = new ArrayList<>(Arrays.asList(student.getArrQuestions()));
        ArrayAdapter adapterQuestion = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1, listTextAnswer);
        listViewQuestions.setAdapter(adapterQuestion);

        ColorDrawable dividerColor = new ColorDrawable(getResources().getColor(R.color.green));
        listViewQuestions.setDivider(dividerColor);
        listViewQuestions.setDividerHeight(2);
    }
}
