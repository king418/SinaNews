package com.king.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.king.SinaNews.R;
import com.king.app.AppContext;
import com.king.model.DownNews;

import java.util.List;

/**
 * AUTHOR: Administrator
 * DATE: 2015/4/22.
 */
public class NewsListAdapter extends BaseAdapter {

    private final AppContext appContext;
    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;

    public NewsListAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
        appContext = AppContext.getInstance();
        inflater = LayoutInflater.from(appContext);
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
        ViewHolder viewHolder;
        DownNews downNews = (DownNews) list.get(position);
        if (ret == null) {
            ret = inflater.inflate(R.layout.item_news_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img = (NetworkImageView) ret.findViewById(R.id.item_img);
            viewHolder.tv_title = (TextView) ret.findViewById(R.id.tv_title);
            viewHolder.tv_title.setTextColor(Color.BLACK);
            viewHolder.tv_content = (TextView) ret.findViewById(R.id.tv_content);
            viewHolder.tv_content.setTextColor(Color.rgb(88,88,88));
            viewHolder.tv_comment_total = (TextView) ret.findViewById(R.id.tv_comment_total);
            viewHolder.tv_comment_total.setTextColor(Color.rgb(88,88,88));
            ret.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) ret.getTag();
        }
        viewHolder.tv_title.setText(downNews.getTitle());
        viewHolder.tv_content.setText(downNews.getContent());
        viewHolder.tv_comment_total.setText(downNews.getComment_total() + "评论");
        String imgUrl = downNews.getCover_pic();
        ImageLoader imageLoader = AppContext.getInstance().getImageLoader();
        viewHolder.img.setImageUrl(imgUrl, imageLoader);
        viewHolder.img.setDefaultImageResId(R.drawable.feed_focus);
        viewHolder.img.setErrorImageResId(R.drawable.feed_focus);
        return ret;
    }

    private class ViewHolder {
        private NetworkImageView img;
        private TextView tv_title, tv_content, tv_comment_total;
    }

}
