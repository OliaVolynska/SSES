package com.example.olia.test.Controller;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.olia.test.Entity.NewsItem;
import com.example.olia.test.Entity.Test;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityTeacherShowTests extends Fragment {

    private DatabaseHandler database;
    private ArrayList arrayList;
    private View rootView;

    public ActivityTeacherShowTests() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.screen_show_tests, container, false);

        Button addTest = (Button) rootView.findViewById(R.id.buttonAddTestInList);
        addTest.setOnClickListener(onClickButtonAdd);

        updateAdapter();
        return rootView;
    }

    private View.OnClickListener onClickButtonAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ActivityTeacherAddTest.class);
            startActivity(intent);
        }
    };

    private ArrayList getListData() {
        database = new DatabaseHandler(this.getContext());
        ArrayList<NewsItem> newsItemTests = new ArrayList<>();
        List<Test> tests = database.getAllTests();
        if (tests.size() != 0) {
            for (Test test : tests) {
                NewsItem newsData = new NewsItem();
                newsData.setHeadline(test.getTitleTest());
                newsData.setSubItem(test.getThemes());
                newsData.setAnotherItem(((Float) test.getMaximalMark()).toString());
                newsItemTests.add(newsData);
            }
        }
        return newsItemTests;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_tests, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit_test:
                editTest(info.position);
                return true;
            case R.id.delete_test:
                deleteTest(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editTest(int position){
        NewsItem newsItem = (NewsItem) arrayList.get(position);
        String titleTest = newsItem.getHeadline();
        Intent intent = new Intent(getContext(), ActivityTeacherEditTest.class);
        intent.putExtra(getResources().getString(R.string.test_for_extra), titleTest);
        startActivity(intent);
    }

    private void deleteTest(int position){
        NewsItem newsItem = (NewsItem) arrayList.get(position);
        String titleTest = newsItem.getHeadline();
        Test test = database.getTest(database.getIdTest(titleTest));
        database.deleteTest(test);
        updateAdapter();
    }

    private void updateAdapter(){
        arrayList = getListData();
        ListView listView = (ListView) rootView.findViewById(R.id.listViewTest);

        listView.setAdapter(new CustomListAdapter(getContext(), arrayList));
        ColorDrawable dividerColor = new ColorDrawable(
                this.getResources().getColor(R.color.green));
        listView.setDivider(dividerColor);
        listView.setDividerHeight(2);
        registerForContextMenu(listView);
    }
}