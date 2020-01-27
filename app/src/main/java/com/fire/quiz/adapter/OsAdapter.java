package com.fire.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fire.quiz.R;

import java.util.ArrayList;

public class OsAdapter extends BaseAdapter {
    private ArrayList<OsItem> osItemList = new ArrayList<OsItem>();

    //OsAdapter 생성자 - 생성해야 튕김 방지.
    public OsAdapter(){

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_os,parent,false);
        }

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_Ostitle);
        TextView tvContent = (TextView)convertView.findViewById(R.id.tv_Oscontent);

        OsItem osItem = osItemList.get(position);

        tvTitle.setText(osItem.getOsTitle());
        tvContent.setText(osItem.getOsContent());

        return convertView;
    }

    public void addItem(String title, String content){
        OsItem item = new OsItem();

        item.setOsTitle(title);
        item.setOsContent(content);

        osItemList.add(item);
    }

    @Override
    public int getCount() {
        return osItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return osItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
