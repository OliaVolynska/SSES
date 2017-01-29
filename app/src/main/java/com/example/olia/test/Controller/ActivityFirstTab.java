package com.example.olia.test.Controller;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.olia.test.R;

public class ActivityFirstTab extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getResources().getString(R.string.teacher));
        tabSpec.setContent(new Intent(this, ActivityTeacherTab.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getResources().getString(R.string.student));
        tabSpec.setContent(new Intent(this, ActivityStudentTab.class));
        tabHost.addTab(tabSpec);

        if (getIntent().getBooleanExtra(getResources().getString(R.string.exit), false)) {
            finish();
        }
    }
}
