package com.example.olia.test.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.olia.test.Entity.User;
import com.example.olia.test.R;

public class ActivityTeacherAddUser extends AppCompatActivity {
    private EditText editTextLogin;
    private EditText editTextName;
    private EditText editTextPass;
    private Button buttonAdd;

    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_user);
        helper = new Helper();

        editTextLogin = (EditText) findViewById(R.id.editTextLoginInAdd);
        editTextName = (EditText) findViewById(R.id.editTextNameInAdd);
        editTextPass = (EditText) findViewById(R.id.editTextPasswordInAdd);
        buttonAdd = (Button) findViewById(R.id.buttonAddInAddUser);

        editTextLogin.addTextChangedListener(textWatcher);
        editTextName.addTextChangedListener(textWatcher);
        editTextPass.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();

        buttonAdd.setOnClickListener(buttonAddListener);
    }

    private TextWatcher textWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForEmptyValues();
        }
    });

    private View.OnClickListener buttonAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAddToDB()) {
                helper.createAlterDialog(ActivityTeacherAddUser.this,
                        R.string.add_new_user, R.string.message_add_user);
            } else {
                helper.makeToastWithId(R.string.message_login_exists,
                        getCurrentFocus());
            }
        }
    };

    private void checkFieldsForEmptyValues() {
        buttonAdd.setEnabled(isCorrectValues());
    }

    private boolean isCorrectValues(){
        return !(helper.checkInput(editTextLogin).isEmpty() || helper.checkInput(editTextName).isEmpty()
                || helper.checkInput(editTextPass).isEmpty());
    }

    private boolean isAddToDB(){
        DatabaseHandler database = new DatabaseHandler(this);
        String login = helper.checkInput(editTextLogin);
        String name = helper.checkInput(editTextName);
        String pass = helper.checkInput(editTextPass);
        User userToAdd = new User(login, name, pass, helper.VALUE_STUDENT);
        boolean isContain = false;
        if(!database.getAllUsers().isEmpty()) {
            for (User user : database.getAllUsers()) {
                if(user.getLogin().equals(login)){
                    isContain = true;
                }
            }
        }
        if(!isContain){
            database.addUser(userToAdd);
            return true;
        }
        else return false;
    }
}
