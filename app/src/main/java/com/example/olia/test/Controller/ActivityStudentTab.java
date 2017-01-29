package com.example.olia.test.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.olia.test.Entity.User;
import com.example.olia.test.R;

import java.util.List;

public class ActivityStudentTab extends Activity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Button takeTheTestButton;
    private EditText editTextLogin, editTextPass;
    private DatabaseHandler database;
    private Helper helper;

    private String[] arrTitleTest;
    private String login, pass, titleTest;

    private SharedPreferences sPref;
    private final String SAVED_LOGIN_ST = "saved_login_st";
    private final String SAVED_PASS_ST = "saved_pass_st";

    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_student_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        database = new DatabaseHandler(this.getBaseContext());
        helper = new Helper();

        Button btnSave = (Button) findViewById(R.id.buttonSaveSt);
        Button btnLoad = (Button) findViewById(R.id.buttonLoadSt);
        editTextLogin = (EditText) findViewById(R.id.editTextLoginStudent);
        editTextPass = (EditText) findViewById(R.id.editTextPassStudent);

        editTextLogin.addTextChangedListener(textWatcher);
        editTextPass.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();

        setAdapter();

        takeTheTestButton.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForEmptyValues();
        }
    });

    private void setAdapter(){
        if (database.getAllTests().isEmpty()){
            arrTitleTest = new String[]{getResources().getString(R.string.no_test)};
        } else {
            arrTitleTest = database.getArrTests();
        }
        Spinner testSpinner = (Spinner) findViewById(R.id.spinnerTest);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityStudentTab.this,
                android.R.layout.simple_spinner_item, arrTitleTest);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testSpinner.setAdapter(adapter);
        testSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonTakeTheTest:
                takeTheTest();
                break;
            case R.id.buttonLoadSt:
                loadData();
                break;
            case R.id.buttonSaveSt:
                saveData();
                break;
        }
    }

    private void checkFieldsForEmptyValues(){
        takeTheTestButton = (Button) findViewById(R.id.buttonTakeTheTest);
        login = helper.checkInput(editTextLogin);
        pass = helper.checkInput(editTextPass);
        if(login.isEmpty() && pass.isEmpty()){
            takeTheTestButton.setEnabled(false);
        }
        else if(titleTest.equals(getResources().getString(R.string.no_test))){
            takeTheTestButton.setEnabled(false);
        }
        else {
            takeTheTestButton.setEnabled(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        titleTest = arrTitleTest[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void saveData() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LOGIN_ST, login);
        ed.putString(SAVED_PASS_ST, pass);
        ed.apply();
        helper.makeToastWithId(R.string.message_data_saved, getCurrentFocus());
    }

    private void loadData() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedLogin = sPref.getString(SAVED_LOGIN_ST, "");
        String savedPass = sPref.getString(SAVED_PASS_ST, "");
        editTextLogin.setText(savedLogin);
        editTextPass.setText(savedPass);
    }

    private void takeTheTest(){
        if(checkTakeTheTest()) {
            Intent intent = new Intent(this.getBaseContext(), ActivityStudentTest.class);
            intent.putExtra(getResources().getString(R.string.login), login);
            intent.putExtra(getResources().getString(R.string.test_for_extra), titleTest);
            startActivity(intent);
        }
        else {
            helper.makeToastWithId(R.string.message_failed_entry, getCurrentFocus());
        }
    }

    private boolean checkTakeTheTest(){
        boolean isSuccess = false;
        List<User> listUser = database.getUsersForRank(1);

        if(!listUser.isEmpty()) {
            for (User user : listUser) {
                if (user.getLogin().equals(login) && user.getPass().equals(pass)) {
                    isSuccess = true;
                }
            }
        }
        return isSuccess;
    }
}
