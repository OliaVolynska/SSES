package com.example.olia.test.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.olia.test.Entity.User;
import com.example.olia.test.R;

import java.util.List;


public class ActivityTeacherTab extends Activity implements View.OnClickListener {
    private DatabaseHandler database;
    private Helper helper;

    private EditText editTextLogin;
    private EditText editTextPassword;

    private SharedPreferences sPref;
    private final String SAVED_LOGIN = "saved_login";
    private final String SAVED_PASS = "saved_pass";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_teacher_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        database = new DatabaseHandler(getBaseContext());
        helper = new Helper();

        if(database.getUsersForRank(helper.VALUE_ADMIN).isEmpty()) {
            database.addUser(new User(1, helper.ADMIN, helper.ADMIN,
                    helper.ADMIN_PASS, helper.VALUE_ADMIN));
        }

        Button enterButton = (Button) findViewById(R.id.buttonEnter);
        Button btnLoad = (Button) findViewById(R.id.buttonLoad);
        Button btnSave = (Button) findViewById(R.id.buttonSave);
        editTextLogin = (EditText) findViewById(R.id.editTextLoginTeacher);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordTeacher);

        enterButton.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonEnter:
                enter(v);
                break;
            case R.id.buttonLoad:
                loadData();
                break;
            case R.id.buttonSave:
                saveData();
                break;
        }
    }

    private void enter(View v){
        if(checkEntry()) {
            Intent intent = new Intent(v.getContext(), ActivityTeacherMain.class);
            startActivity(intent);
        }
        else {
            helper.makeToastWithId(R.string.message_failed_entry, getCurrentFocus());
        }
    }

    private void saveData() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LOGIN, editTextLogin.getText().toString());
        ed.putString(SAVED_PASS, editTextPassword.getText().toString());
        ed.apply();
        helper.makeToastWithId(R.string.message_data_saved, getCurrentFocus());
    }

    private void loadData() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedLogin = sPref.getString(SAVED_LOGIN, "");
        String savedPass = sPref.getString(SAVED_PASS, "");
        editTextLogin.setText(savedLogin);
        editTextPassword.setText(savedPass);
    }

    private boolean checkEntry(){
        boolean isSuccess = false;
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        List<User> listAdmin = database.getUsersForRank(0);

        if(!listAdmin.isEmpty()) {
            for (User user : listAdmin) {
                if (user.getLogin().equals(login) && user.getPass().equals(password)) {
                    isSuccess = true;
                }
            }
        }
        return isSuccess;
    }
}
