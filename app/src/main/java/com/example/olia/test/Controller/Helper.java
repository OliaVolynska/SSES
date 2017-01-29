package com.example.olia.test.Controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olia.test.Controller.ActivityTeacherMain;
import com.example.olia.test.R;

public class Helper extends Activity{

    public final int VALUE_TRUE = 0;
    public final int VALUE_FALSE = 1;

    public final int VALUE_ADMIN = 0;
    public final int VALUE_STUDENT = 1;
    public final String ADMIN = "Admin";
    public final String ADMIN_PASS = "admin000";

    public Helper(){}

    public void setTextView(TextView textView, String str){
        textView.setText(textView.getText() + " " + str);
    }

    public String checkInput(EditText str){
        return str.getText().toString().replaceAll("\\s+", " ").trim();
    }

    public void makeToastWithId(int idMessage, View v){
        Toast toast = Toast.makeText(v.getContext(), idMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,-105);
        toast.show();
    }

    public void makeToastWithStr(String message, View v){
        Toast toast = Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,-105);
        toast.show();
    }

    public void createAlterDialog(final Activity activity, int idTitle, int idMessage){
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);

        ad.setTitle(idTitle);
        ad.setMessage(idMessage);
        ad.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, activity.getClass());
                activity.startActivity(intent);
            }
        });
        ad.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, ActivityTeacherMain.class);
                activity.startActivity(intent);
            }
        });
        ad.setCancelable(false);
        ad.show();
    }

    public void createDialogAfterEdit(final Activity activity, int idTitle, int idMessage){
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        ad.setTitle(idTitle);
        ad.setMessage(idMessage);
        ad.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity,
                        ActivityTeacherMain.class);
                activity.startActivity(intent);
            }
        });
        ad.setCancelable(false);
        ad.show();
    }
}
