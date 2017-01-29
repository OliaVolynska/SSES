package com.example.olia.test.Controller;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.example.olia.test.Entity.User;
import com.example.olia.test.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityTeacherShowUsers extends Fragment {
    private DatabaseHandler database;
    private ArrayList arrayList;
    private View rootView;

    public ActivityTeacherShowUsers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_teacher_show_users, container, false);

        Button addUser = (Button) rootView.findViewById(R.id.buttonAddNewUser);
        addUser.setOnClickListener(onClickButtonAdd);

        updateAdapter();
        return rootView;
    }

    private View.OnClickListener onClickButtonAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ActivityTeacherAddUser.class);
            startActivity(intent);
        }
    };

    private ArrayList getListData() {
        database = new DatabaseHandler(this.getContext());
        Helper helper = new Helper();
        ArrayList<NewsItem> newsItemsUser = new ArrayList<>();
        List<User> students = database.getUsersForRank(helper.VALUE_STUDENT);
        if (students.size() != 0) {
            for (User user :students) {
                NewsItem newsData = new NewsItem();
                newsData.setHeadline(user.getFullName());
                newsData.setSubItem(user.getLogin());
                newsData.setAnotherItem(user.getPass());
                newsItemsUser.add(newsData);
            }
        }
        return newsItemsUser;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_users, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit_user:
                editUser(info.position);
                return true;
            case R.id.delete_user:
                deleteUser(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editUser(int position){
        int idUser = getIdUserInPosition(position);
        Intent intent = new Intent(getContext(), ActivityTeacherEditUser.class);
        intent.putExtra(getResources().getString(R.string.id_user),((Integer)idUser).toString());
        startActivity(intent);
    }

    private void deleteUser(int position){
        User user = database.getUser(getIdUserInPosition(position));
        database.deleteUser(user);
        updateAdapter();
    }

    private int getIdUserInPosition(int position){
        NewsItem newsItem = (NewsItem) arrayList.get(position);
        String login = newsItem.getSubItem();
        return database.getIdUser(login);
    }

    private void updateAdapter(){
        arrayList = getListData();
        final ListView listView = (ListView) rootView.findViewById(R.id.listViewUsers);

        listView.setAdapter(new CustomListAdapter(getContext(), arrayList));
        ColorDrawable dividerColor = new ColorDrawable(
                this.getResources().getColor(R.color.green));
        listView.setDivider(dividerColor);
        listView.setDividerHeight(2);

        registerForContextMenu(listView);
    }
}