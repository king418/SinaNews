package com.king.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.king.SinaNews.R;
import com.king.app.AppContext;
import com.king.model.VideoNews;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/23.
 */
public class VieoNewsAdapter extends BaseAdapter {
    private List<VideoNews> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public VieoNewsAdapter(List<VideoNews> list) {
        this.list = list;
        context = AppContext.getInstance();
        inflater = LayoutInflater.from(context);
        imageLoader = AppContext.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        Object ret = null;
        if (list != null) {
            ret = list.get(position);
        }
        return ret;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = convertView;
        ViewHolder holder;
        if (ret == null) {
            ret = inflater.inflate(R.layout.item_videonews_list, parent, false);
            holder = new ViewHolder();
            holder.video_img = (NetworkImageView) ret.findViewById(R.id.video_img);
            holder.video_title = (TextView) ret.findViewById(R.id.video_title);
            holder.video_playnum = (TextView) ret.findViewById(R.id.video_playnum);
            ret.setTag(holder);
        }else {
            holder = (ViewHolder) ret.getTag();
        }

        VideoNews videoNews = list.get(position);
        holder.video_title.setText(videoNews.getTitle());
        holder.video_playnum.setText(videoNews.getPlay_num()+"播放");
        String pic_url = videoNews.getPic();
        holder.video_img.setImageUrl(pic_url,imageLoader);
        holder.video_img.setDefaultImageResId(R.drawable.feed_focus);
        holder.video_img.setErrorImageResId(R.drawable.feed_focus);
        return ret;
    }


    class ViewHolder {
        private NetworkImageView video_img;
        private TextView video_title;
        private TextView video_playnum;
    }

}
