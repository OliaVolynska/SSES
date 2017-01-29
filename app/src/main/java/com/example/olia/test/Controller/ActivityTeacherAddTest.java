package com.example.olia.test.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.olia.test.Entity.Test;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityTeacherAddTest extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    private EditText editTextTitleTest, editTextMark;
    private Button buttonAddTest;
    private ListView listViewThemes;
    private List<String> checkedThemes;
    private boolean allowButtonAdd = false;

    private DatabaseHandler database;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_test_activity);
        database = new DatabaseHandler(this);
        helper = new Helper();

        editTextTitleTest = (EditText) findViewById(R.id.editTextTitleTest);
        editTextMark = (EditText) findViewById(R.id.editTextMaximalMark);
        buttonAddTest = (Button) findViewById(R.id.buttonAddTest);
        Button buttonAddTheme = (Button) findViewById(R.id.buttonAddThemeInTest);

        buttonAddTest.setOnClickListener(this);
        buttonAddTheme.setOnClickListener(this);

        editTextTitleTest.addTextChangedListener(textWatcher);
        editTextMark.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();

        setAdapter();
    }

    private void setAdapter(){
        listViewThemes = (ListView) findViewById(R.id.listThemes);
        List<String> listTextThemes = new ArrayList<>(Arrays.asList(database.getArrThemes()));
        ArrayAdapter<String> adapter;
        if(listTextThemes.isEmpty()){
            listTextThemes.add(getResources().getString(R.string.no_theme));
            adapter = new ArrayAdapter<>(getBaseContext(),
                    android.R.layout.simple_list_item_1, listTextThemes);
        }
        else {
            listViewThemes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new ArrayAdapter<>(getBaseContext(),
                    android.R.layout.simple_list_item_multiple_choice, listTextThemes);
            listViewThemes.setOnItemClickListener(this);
        }
        listViewThemes.setAdapter(adapter);
    }

    private TextWatcher textWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForEmptyValues();
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAddThemeInTest:
                Intent intent = new Intent(v.getContext(), ActivityTeacherMain.class);
                startActivity(intent);
                break;
            case R.id.buttonAddTest:
                addTest();
                break;
        }
    }

    private void addTest(){
        String themes = "";
        for (String item:checkedThemes) {
            themes += item + "\n";
        }
        Test testToAdd = new Test(helper.checkInput(editTextTitleTest),
                Float.parseFloat(editTextMark.getText().toString()), themes);
        if(isContainTest()) {
            helper.makeToastWithId(R.string.message_test_exists, getCurrentFocus());
        }
        else {
            database.addTest(testToAdd);
            helper.createAlterDialog(ActivityTeacherAddTest.this,
                    R.string.test, R.string.testMessage);
        }
    }

    private boolean isContainTest(){
        boolean isContain = false;
        for (Test item:database.getAllTests()) {
            if(item.getTitleTest().equals(helper.checkInput(editTextTitleTest))){
                isContain = true;
            }
        }
        return isContain;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int countChoice = listViewThemes.getCount();
        checkedThemes = new ArrayList<>();

        SparseBooleanArray sparseBooleanArray = listViewThemes.getCheckedItemPositions();
        for (int i = 0; i < countChoice; i++){
            if(sparseBooleanArray.get(i)){
                checkedThemes.add(listViewThemes.getItemAtPosition(i).toString());
            }
        }
        allowButtonAdd = !checkedThemes.isEmpty();
        checkFieldsForEmptyValues();
    }

    private void checkFieldsForEmptyValues(){
            buttonAddTest.setEnabled(isCorrectValues());
    }

    private boolean isCorrectValues(){
        return !(helper.checkInput(editTextTitleTest).isEmpty()
                || helper.checkInput(editTextMark).isEmpty() || !allowButtonAdd);
    }
}
