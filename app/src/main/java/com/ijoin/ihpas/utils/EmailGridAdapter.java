package com.ijoin.ihpas.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ijoin.ihpas.R;

import java.util.List;

public class EmailGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> emailList;
    private LayoutInflater inflater;

    public EmailGridAdapter(Context context, List<String> emailList) {
        this.context = context;
        this.emailList = emailList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return emailList.size();
    }

    @Override
    public Object getItem(int position) {
        return emailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_email_grid, parent, false);
            holder = new ViewHolder();
            holder.emailText = convertView.findViewById(R.id.email_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String email = emailList.get(position);
        holder.emailText.setText(email);

        return convertView;
    }

    static class ViewHolder {
        TextView emailText;
    }
}
