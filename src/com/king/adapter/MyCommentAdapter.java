package com.king.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.king.SinaNews.R;
import com.king.model.MyComment;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/27.
 */
public class MyCommentAdapter extends BaseAdapter {

    private Context context;
    private List<MyComment> list;
    private LayoutInflater inflater;

    public MyCommentAdapter(Context context, List<MyComment> list) {
        this.context = context;
        this.list = list;
        inflater  = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_mycomment_list,parent,false);
            holder = new ViewHolder();
            holder.item_mycomment_title = (TextView) convertView.findViewById(R.id.item_mycomment_title);
            holder.item_mycomment_creattime = (TextView) convertView.findViewById(R.id.item_mycomment_creattime);
            holder.item_mycomment_content = (TextView) convertView.findViewById(R.id.item_mycomment_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyComment myComment = list.get(position);
        holder.item_mycomment_content.setText(myComment.getContent());
        holder.item_mycomment_creattime.setText(myComment.getCreate_time());
        holder.item_mycomment_title.setText(myComment.getTitle());
        return convertView;
    }

    class ViewHolder{
        private TextView item_mycomment_title;
        private TextView item_mycomment_content;
        private TextView item_mycomment_creattime;
    }
}
