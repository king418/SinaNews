package com.king.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.king.SinaNews.R;
import com.king.app.AppContext;
import com.king.model.ImageNews;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/22.
 */
public class ImageNewsAdapter extends BaseAdapter {
    private Context context;
    private List<ImageNews> list;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public ImageNewsAdapter(Context context, List<ImageNews> list) {
        this.context = context;
        this.list = list;
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
            ret = inflater.inflate(R.layout.item_imgnews_list, parent, false);
            holder = new ViewHolder();
            holder.image1 = (NetworkImageView) ret.findViewById(R.id.image1);
            holder.image2 = (NetworkImageView) ret.findViewById(R.id.image2);
            holder.image3 = (NetworkImageView) ret.findViewById(R.id.image3);
            holder.imglist_title = (TextView) ret.findViewById(R.id.imglist_title);
            holder.imglist_content = (TextView) ret.findViewById(R.id.imglist_content);
            holder.img_container = (LinearLayout) ret.findViewById(R.id.imag_container);
            ret.setTag(holder);
        } else {
            holder = (ViewHolder) ret.getTag();
        }
        ImageNews imageNews = list.get(position);
        holder.imglist_title.setText(imageNews.getTitle());
        holder.imglist_content.setText(imageNews.getContent());
        List<String> urls = imageNews.getPic_list();
        if (urls != null) {
            int size = urls.size();
            switch (size) {
                case 0:
                    holder.image1.setVisibility(View.GONE);
                    holder.img_container.setVisibility(View.GONE);
                    break;
                case 1:
                    holder.image1.setVisibility(View.VISIBLE);
                    holder.img_container.setVisibility(View.GONE);
                    setImage(holder.image1, urls.get(0));
                    break;
                case 2:
                    holder.image1.setVisibility(View.GONE);
                    holder.img_container.setVisibility(View.VISIBLE);
                    setImage(holder.image2,urls.get(0));
                    setImage(holder.image3,urls.get(1));
                    break;
                default:
                    holder.image1.setVisibility(View.VISIBLE);
                    holder.img_container.setVisibility(View.VISIBLE);
                    setImage(holder.image1, urls.get(0));
                    setImage(holder.image2, urls.get(1));
                    setImage(holder.image3, urls.get(2));
                    break;

            }
        }else {
            holder.image1.setVisibility(View.GONE);
            holder.img_container.setVisibility(View.GONE);
        }
        return ret;
    }

    private void setImage(NetworkImageView imageView, String url) {
        imageView.setImageUrl(url, imageLoader);
        imageView.setDefaultImageResId(R.drawable.feed_focus);
        imageView.setErrorImageResId(R.drawable.feed_focus);
    }

    private class ViewHolder {
        private NetworkImageView image1, image2, image3;
        private TextView imglist_title, imglist_content;
        private LinearLayout img_container;
    }

}
