package com.example.olia.test.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ActivityTeacherEditTest extends AppCompatActivity
        implements AdapterView.OnItemClickListener{
    private EditText editTextTitleTest, editTextMark;
    private Button buttonEdit;
    private ListView listViewThemes;

    private Helper helper;
    private DatabaseHandler database;
    private List<String> checkedThemes;
    private boolean allowButtonAdd = true;
    private Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_test);

        helper = new Helper();
        database = new DatabaseHandler(this);
        test = database.getTest(database.getIdTest(getIntent().getStringExtra(
                getResources().getString(R.string.test_for_extra))));
        checkedThemes = new ArrayList<>();

        editTextTitleTest = (EditText) findViewById(R.id.editTextTitleTestInEdit);
        editTextMark = (EditText) findViewById(R.id.editTextMaximalMarkInEdit);
        buttonEdit = (Button) findViewById(R.id.buttonEditTest);

        buttonEdit.setOnClickListener(editClick);
        editTextTitleTest.setText(test.getTitleTest());
        editTextMark.setText(((Float)test.getMaximalMark()).toString());
        editTextTitleTest.addTextChangedListener(textWatcher);
        editTextMark.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();

        setAdapter();
    }

    private TextWatcher textWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForEmptyValues();
        }
    });

    private View.OnClickListener editClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editTest();
        }
    };

    private void editTest(){
        String themes = "";
        if(!checkedThemes.isEmpty()) {
            for (String item : checkedThemes) {
                themes += item + "\n";
            }
            test.setThemes(themes);
        }
        if(isContainTest()) {
            helper.makeToastWithId(R.string.message_test_exists, getCurrentFocus());
        }
        else {
            test.setTitleTest(helper.checkInput(editTextTitleTest));
            test.setMaximalMark(Float.parseFloat(helper.checkInput(editTextMark)));
            database.updateTest(test);
            helper.createDialogAfterEdit(ActivityTeacherEditTest.this,
                    R.string.edit, R.string.message_test_edited);
        }
    }

    private boolean isContainTest(){
        boolean isContain = false;
        for (Test item:database.getAllTests()) {
            if(item.getTitleTest().equals(helper.checkInput(editTextTitleTest))
                    && !item.getTitleTest().equals(test.getTitleTest())){
                isContain = true;
            }
        }
        return isContain;
    }

    private void setAdapter(){
        listViewThemes = (ListView) findViewById(R.id.listThemesInEdit);
        List<String> listTextThemes = new ArrayList<>(Arrays.asList(database.getArrThemes()));
        ArrayAdapter<String> adapter;
        listViewThemes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_multiple_choice, listTextThemes);
        listViewThemes.setOnItemClickListener(this);
        listViewThemes.setAdapter(adapter);
        checkedItem(listTextThemes);
    }

    private void checkedItem(List<String> listThemes){
        int index = 0;
        for(String theme:listThemes) {
            for (String choose:test.getArrThemes()) {
                if(theme.equals(choose)){
                    listViewThemes.setItemChecked(index, true);
                }
            }
            index++;
        }
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
            buttonEdit.setEnabled(isCorrectValues());
    }

    private boolean isCorrectValues(){
        return !(helper.checkInput(editTextTitleTest).isEmpty()
                || helper.checkInput(editTextMark).isEmpty() || !allowButtonAdd
                || (helper.checkInput(editTextTitleTest).equals(test.getTitleTest())
                && Float.parseFloat(helper.checkInput(editTextMark)) == test.getMaximalMark())
                && checkedThemes.isEmpty());
    }
}
