package com.example.olia.test.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.olia.test.Entity.NewsItem;
import com.example.olia.test.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<NewsItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<NewsItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.subItemView = (TextView) convertView.findViewById(R.id.sub_item);
            holder.anotherItemView = (TextView) convertView.findViewById(R.id.another_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).getHeadline());
        holder.subItemView.setText(listData.get(position).getSubItem());
        holder.anotherItemView.setText(listData.get(position).getAnotherItem());
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView subItemView;
        TextView anotherItemView;
    }
}