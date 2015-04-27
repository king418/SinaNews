package com.king.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.king.SinaNews.R;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/27.
 */
public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> list;
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<Bitmap> list) {
        this.context = context;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_gv_img,parent,false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_gvi_img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap(list.get(position));

        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
    }

}
