package com.example.olia.test.Controller;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.olia.test.Entity.Question;
import com.example.olia.test.Entity.Theme;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityTeacherShowThemes extends Fragment {
    private DatabaseHandler database;
    private ArrayAdapter adapterTheme;
    private List<String> listThemes;
    private View rootView;

    public ActivityTeacherShowThemes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.screen_show_all_themes, container, false);
        database = new DatabaseHandler(this.getContext());
        setListThemes();
        searchTheme();
        return rootView;
    }

    private void setListThemes(){
        ListView listView = (ListView) rootView.findViewById(R.id.listViewThemes);
        String[] textThemes = new String[]{};
        if(database.getArrThemes().length!=0){
                    textThemes = database.getArrThemes();
        }
        listThemes = new ArrayList<>();
        Collections.addAll(listThemes, textThemes);

        adapterTheme = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_list_item_1, listThemes);
        listView.setAdapter(adapterTheme);
        ColorDrawable dividerColor = new ColorDrawable(
                this.getResources().getColor(R.color.green));
        listView.setDivider(dividerColor);
        listView.setDividerHeight(2);
        listView.setOnItemClickListener(onItemClickListener);
        registerForContextMenu(listView);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            String themeStr = listThemes.get(position);
            int idTheme = database.getIdTheme(themeStr);
            createQuestionsAlertDialog(database.getTheme(idTheme));
        }
    };

    private void createQuestionsAlertDialog(Theme theme) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.questions));
        List<Question> questionList= database.getQuestionForTheme(theme.getIdTheme());
        List<String> questionsStrList = new ArrayList<>();
        for (Question question: questionList) {
            questionsStrList.add(question.getTextQuestion());
        }
        String[] questions = questionsStrList.toArray(new String[questionsStrList.size()]);

        builder.setItems(questions , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
            }
        });
        builder.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_themes, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_themes:
                deleteItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteItem(int position){
        String themeStr = listThemes.get(position);
        int idTheme = database.getIdTheme(themeStr);
        adapterTheme.remove(adapterTheme.getItem(position));
        adapterTheme.notifyDataSetChanged();

        database.deleteTheme(database.getTheme(idTheme));
    }

    private void searchTheme(){
        EditText inputSearch = (EditText) rootView.findViewById(R.id.editTextSearchThemes);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ActivityTeacherShowThemes.this.adapterTheme.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }
}
