package com.king.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.king.SinaNews.R;
import com.king.model.activity.NewsComment;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/24.
 */
public class CommentListAdapter extends BaseAdapter {

    private List<NewsComment> list;
    private Context context;
    private LayoutInflater inflater;

    public CommentListAdapter(List<NewsComment> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_comment,parent,false);
            holder = new ViewHolder();
            holder.item_comment_time = (TextView) convertView.findViewById(R.id.item_comment_time);
            holder.item_comment_content = (TextView) convertView.findViewById(R.id.item_comment_content);
            holder.item_comment_user = (TextView) convertView.findViewById(R.id.item_comment_user);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        NewsComment newsComment = list.get(position);
        holder.item_comment_time.setText(newsComment.getCreate_time());
        holder.item_comment_user.setText(newsComment.getUser());
        holder.item_comment_content.setText(newsComment.getContent());

        return convertView;
    }

    private class ViewHolder{
        TextView item_comment_time;
        TextView item_comment_user;
        TextView item_comment_content;
    }
}
