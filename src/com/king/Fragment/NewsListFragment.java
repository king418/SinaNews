package com.king.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.king.SinaNews.ImageDetailActivity;
import com.king.SinaNews.R;
import com.king.SinaNews.TextNewsContentActivity;
import com.king.SinaNews.VideoDetailActivity;
import com.king.adapter.HeadPagerAdapter;
import com.king.adapter.ImageNewsAdapter;
import com.king.adapter.NewsListAdapter;
import com.king.adapter.VieoNewsAdapter;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.DownNews;
import com.king.model.ImageNews;
import com.king.model.TopNews;
import com.king.model.VideoNews;
import com.king.pulltorefresh.PullToRefreshBase;
import com.king.pulltorefresh.PullToRefreshListView;
import com.king.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/4/22.
 */
public class NewsListFragment extends Fragment {

    private String url;
    private List<Object> downNewses;
    private ListView list_news;
    private RequestQueue requestQueue;
    private List<Object> topNewses;
    private View headView;
    private ViewPager headImg;
    private TextView headTitle;
    private LinearLayout container_dots;
    private ImageLoader imageLoader;
    private List<View> imgList;
    private ImageView[] imgs;
    private int news_type_id;
    private PullToRefreshListView refreshListView;
    private int currentPage = 1;

    private List<ImageNews> imageList;
    private List<VideoNews> videoList;
    private NewsListAdapter newsListAdapter;
    private ImageNewsAdapter imageNewsAdapter;
    private VieoNewsAdapter vieoNewsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = AppContext.getInstance().getRequestQueue();
        imageLoader = AppContext.getInstance().getImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, container, false);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.list_content);
        list_news = refreshListView.getRefreshableView();
        url = initUrl();
        LinearLayout empty = (LinearLayout) view.findViewById(R.id.empty);
        list_news.setEmptyView(empty);
        addRefreshListener();
        setListView(url + currentPage);
        return view;
    }


    private void setListView(String urlStr) {
        StringRequest request = new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i("NewsLOGFragment", "-------->news_type_id:　" + news_type_id);
                switch (news_type_id) {

                    case 0:
                        reloadTextListView(response);
                        break;
                    case 1:
                        reloadImageListView(response);
                        break;
                    case 2:
                        reloadVideoListView(response);
                        break;
                }
                refreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    ///////////////////////////设置下拉刷新//////////////////////////////////////
    private void addRefreshListener() {
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                setListView(url + currentPage);
            }
        });
        refreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                setListView(url + currentPage);
            }
        });
    }

    ///////////////////////////设置listView头视图//////////////////////////////////
    private void initHead() {
        headView = LayoutInflater.from(AppContext.getInstance()).inflate(R.layout.head_viewpager, null);
        headImg = (ViewPager) headView.findViewById(R.id.head_viewpager);
        headTitle = (TextView) headView.findViewById(R.id.head_title);
        container_dots = (LinearLayout) headView.findViewById(R.id.container_dots);
        imgList = new ArrayList<View>();
        setPagerImg(topNewses);
        addHeadListener(headImg);
        setCurrentTitle(0);
    }

    private void setPagerImg(List<Object> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TopNews topNews = (TopNews) list.get(i);
            String imgUrl = topNews.getCover_pic();
            setHeadImage(imgUrl);
        }
        headImg.setAdapter(new HeadPagerAdapter(imgList));
        initDots(size);
    }

    /**
     * 图片添加到Viewpager需要的list中
     *
     * @param imgUrl
     */
    private void setHeadImage(String imgUrl) {
        NetworkImageView imageView = new NetworkImageView(AppContext.getInstance());
        imageView.setImageUrl(imgUrl, imageLoader);
        imageView.setDefaultImageResId(R.drawable.feed_focus);
        imageView.setErrorImageResId(R.drawable.feed_focus);
        imgList.add(imageView);

    }

    /**
     * 初始化定位下标圆点
     *
     * @param size 当前list的size
     */
    private void initDots(int size) {
        imgs = new ImageView[size];
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(AppContext.getInstance());
            imageView.setEnabled(true);
            imageView.setImageResource(R.drawable.dots_state);
            container_dots.addView(imageView);
            imgs[i] = imageView;
        }
        imgs[0].setEnabled(false);
    }


    private void addHeadListener(ViewPager view) {
        if (view != null) {
            view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentDot(position);
                    setCurrentTitle(position);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }

    /**
     * 设置当前点
     *
     * @param position
     */
    private void setCurrentDot(int position) {
        imgs[position].setEnabled(false);
        int length = imgs.length;
        for (int i = 0; i < length; i++) {
            if (i != position) {
                imgs[i].setEnabled(true);
            }
        }
    }

    private void setCurrentTitle(int position) {
        TopNews topNews = (TopNews) topNewses.get(position);
        headTitle.setText(topNews.getTitle());
    }

    ////////////////////////////////////////////////////////
    private String initUrl() {
        String ret = "";
        Bundle bundle = getArguments();
        String cate_id = bundle.getString("cate_id");
        news_type_id = bundle.getInt("type");
        switch (news_type_id) {
            case 0:
                ret = Constants.NEWS_LIST + cate_id + "&p=";
                setTextListView();
                break;
            case 1:
                ret = Constants.PIC_NEWS_LIST + cate_id + "&p=";
                setImageLisetView();
                break;
            case 2:
                ret = Constants.VIDEO_NEWS_LIST + cate_id + "&p=";
                setVideoListView();
                break;
        }
        return ret;
    }

    /**
     * 加载普通新闻
     */
    private void setTextListView() {
        downNewses = new ArrayList<Object>();
        newsListAdapter = new NewsListAdapter(getActivity(), downNewses);
        list_news.setAdapter(newsListAdapter);
        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = list_news.getHeaderViewsCount();
                DownNews downNews = (DownNews) downNewses.get(position - headerViewsCount);
                Intent intent = new Intent(getActivity(), TextNewsContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id", downNews.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 加载图片新闻列表页面
     */
    private void setImageLisetView() {
        //Log.i("NewsList", "---------->" + imageNewses.size());
        imageList = new ArrayList<ImageNews>();
        imageNewsAdapter = new ImageNewsAdapter(AppContext.getInstance(), imageList);
        list_news.setAdapter(imageNewsAdapter);
        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageNews imageNews = imageList.get(position);
                Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id",imageNews.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 加载VideoNews   ListView
     */
    private void setVideoListView() {
        videoList = new ArrayList<VideoNews>();
        vieoNewsAdapter = new VieoNewsAdapter(videoList);
        list_news.setAdapter(vieoNewsAdapter);
        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoNews videoNews = videoList.get(position);
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                Bundle bundle= new Bundle();
                bundle.putString("video_url",videoNews.getVideo_url());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void reloadTextListView(String jsonStr) {
        Map<String, List<Object>> map = JsonUtils.parseTextNews(jsonStr);
        if (currentPage == 1) {
            downNewses.clear();
            list_news.removeHeaderView(headView);
            topNewses = map.get("top_news");
            if (topNewses != null && topNewses.size() != 0) {
                initHead();
                list_news.addHeaderView(headView);
            }
        }
        List<Object> down = map.get("down_news");
        downNewses.addAll(down);
        newsListAdapter.notifyDataSetChanged();
    }

    private void reloadImageListView(String jsonStr) {
        if (currentPage == 1) {
            imageList.clear();
        }
        List<ImageNews> imageNewses = JsonUtils.parseImageNews(jsonStr);
        imageList.addAll(imageNewses);
        imageNewsAdapter.notifyDataSetChanged();
    }

    private void reloadVideoListView(String jsonStr) {
        if (currentPage == 1) {
            videoList.clear();
        }
        List<VideoNews> videoNewses = JsonUtils.parseVideoNews(jsonStr);
        videoList.addAll(videoNewses);
        vieoNewsAdapter.notifyDataSetChanged();
    }

}
