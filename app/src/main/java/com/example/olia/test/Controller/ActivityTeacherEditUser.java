package com.example.olia.test.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.olia.test.Entity.User;
import com.example.olia.test.R;

public class ActivityTeacherEditUser extends AppCompatActivity {

    private EditText editTextLogin, editTextName, editTextPass;
    private Button buttonEdit;
    private Helper helper;
    private DatabaseHandler database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_user);
        database = new DatabaseHandler(this);
        helper = new Helper();

        int idUser = Integer.parseInt(getIntent().getStringExtra(getResources().getString(R.string.id_user)));
        user = database.getUser(idUser);

        editTextLogin = (EditText) findViewById(R.id.editTextLoginInEdit);
        editTextName = (EditText) findViewById(R.id.editTextNameInEdit);
        editTextPass = (EditText) findViewById(R.id.editTextPasswordInEdit);
        buttonEdit = (Button) findViewById(R.id.buttonEditInEditUser);

        editTextLogin.setText(user.getLogin());
        editTextName.setText(user.getFullName());
        editTextPass.setText(user.getPass());

        editTextLogin.addTextChangedListener(textWatcher);
        editTextName.addTextChangedListener(textWatcher);
        editTextPass.addTextChangedListener(textWatcher);
        checkFieldsForValues();

        buttonEdit.setOnClickListener(onClickEdit);
    }

    private TextWatcher textWatcher = new TextWatcherHelper().textWatcher(new Function() {
        @Override
        public void CheckFields() {
            checkFieldsForValues();
        }
    });

    private View.OnClickListener onClickEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEdit()) {
                helper.createDialogAfterEdit(ActivityTeacherEditUser.this, R.string.edit, R.string.message_user_edited);
            }
            else {
                helper.makeToastWithId(R.string.message_login_exists, getCurrentFocus());
            }
        }
    };

    private void checkFieldsForValues(){
        buttonEdit.setEnabled(isCorrectValues());
    }

    private boolean isCorrectValues(){
        return !(helper.checkInput(editTextLogin).isEmpty() || helper.checkInput(editTextName).isEmpty()
                || helper.checkInput(editTextPass).isEmpty()
                || (helper.checkInput(editTextLogin).equals(user.getLogin())
                && helper.checkInput(editTextName).equals(user.getFullName())
                && helper.checkInput(editTextPass).equals(user.getPass())));
    }

    private boolean isEdit(){
        if(!isContainLogin()){
            user.setLogin(helper.checkInput(editTextLogin));
            user.setFullName(helper.checkInput(editTextName));
            user.setPass(helper.checkInput(editTextPass));
            database.updateUser(user);
            return true;
        }
        else return false;
    }

    private boolean isContainLogin(){
        boolean isContain = false;
        String login = helper.checkInput(editTextLogin);
        for (User item : database.getAllUsers()) {
            if (item.getLogin().equals(login) &&
                    !user.getLogin().equals(login)) {
                isContain = true;
            }
        }
        return isContain;
    }
}
