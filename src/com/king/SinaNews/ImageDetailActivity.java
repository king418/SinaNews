package com.king.SinaNews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.king.adapter.HeadPagerAdapter;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.activity.TextNewsDetail;
import com.king.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/24.
 */
public class ImageDetailActivity extends Activity {

    //   private Button btn_imgDetail_back;
    //   private Button btn_imgDetail_share;
    //  private Button btn_imgDetail_favorite;

    private ViewPager vp_imgDetail_pic;
    private TextView tv_imgDetail_title;
    private TextView tv_imgDetail_page;
    private TextView tv_imgDetail_content;
    private ProgressDialog dialog;

    private String imgDetailUrl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private List<View> pager;
    private HeadPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedetail);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        // btn_imgDetail_back = (Button) findViewById(R.id.btn_picdetail_back);
        //btn_imgDetail_favorite = (Button) findViewById(R.id.btn_picdetail_favorite);
        // btn_imgDetail_share = (Button) findViewById(R.id.btn_picdetail_share);

        vp_imgDetail_pic = (ViewPager) findViewById(R.id.viewPager_picdetail_pic);
        tv_imgDetail_content = (TextView) findViewById(R.id.textView_picdetail_content);
        tv_imgDetail_page = (TextView) findViewById(R.id.textView_picdetail_page);
        tv_imgDetail_title = (TextView) findViewById(R.id.textView_picdetail_title);
        dialog = new ProgressDialog(this);
        dialog.setMessage("图片加载中。。。");
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String news_id = bundle.getString("news_id");
        imgDetailUrl = Constants.NEWS_DETAIL + news_id;
        requestQueue = AppContext.getInstance().getRequestQueue();
        imageLoader = AppContext.getInstance().getImageLoader();
        pager = new ArrayList<View>();
        shareData();
    }

    private void shareData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(imgDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextNewsDetail newsDetail = JsonUtils.parseTextNewsDetail(response);
                List<String> pic_list = newsDetail.getPic_list();
                if (pic_list != null && pic_list.size() > 0) {
                    int size = pic_list.size();
                    for (int i = 0; i < size; i++) {
                        NetworkImageView imageView = new NetworkImageView(ImageDetailActivity.this);
                        imageView.setImageUrl(pic_list.get(i), imageLoader);
                        imageView.setDefaultImageResId(R.drawable.feed_focus);
                        imageView.setErrorImageResId(R.drawable.feed_focus);
                        pager.add(imageView);
                    }
                    adapter = new HeadPagerAdapter(pager);
                    vp_imgDetail_pic.setAdapter(adapter);
                    tv_imgDetail_page.setText("1/" + size);
                }
                tv_imgDetail_content.setText(newsDetail.getContent());
                tv_imgDetail_title.setText(newsDetail.getTitle());
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }


    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.btn_picdetail_back:
                finish();
                break;
            case R.id.btn_picdetail_favorite:

                break;
            case R.id.btn_picdetail_share:

                break;
        }
    }

    private void addListener() {
        vp_imgDetail_pic.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tv_imgDetail_page.setText("" + (i+1) + "/" + pager.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}