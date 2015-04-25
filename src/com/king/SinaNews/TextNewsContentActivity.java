package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.activity.NewsComment;
import com.king.model.activity.TextNewsDetail;
import com.king.utils.JsonUtils;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/23.
 */
public class TextNewsContentActivity extends Activity {
    private NetworkImageView netImg;
    private TextNewsDetail newsDetail;
    private List<NewsComment> newsComments;

    private ImageView img_textcontent_back;
    private ImageView img_textcontent_share;
    private ImageView img_textcontent_favorite;
    private TextView tv_textcontent_time;
    private TextView tv_textcontent_content;
    private TextView tv_textcontent_title;
    private TextView tv_textcontent_morecommet;
    private LinearLayout container_commet;
    private EditText et_textcontent_commet;
    private Button btn_textcontent_submit;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private String detailUrl;
    private String commentUrl;
    private String news_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textnewscontent);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        img_textcontent_back = (ImageView) findViewById(R.id.img_textcontent_back);
        img_textcontent_share = (ImageView) findViewById(R.id.img_textcontent_share);
        img_textcontent_favorite = (ImageView) findViewById(R.id.img_textcontent_favorite);
        netImg = (NetworkImageView) findViewById(R.id.img_textcontent_net);
        tv_textcontent_time = (TextView) findViewById(R.id.tv_textcontent_time);
        tv_textcontent_content = (TextView) findViewById(R.id.tv_textcontent_content);
        tv_textcontent_title = (TextView) findViewById(R.id.tv_textcontent_title);
        tv_textcontent_morecommet = (TextView) findViewById(R.id.tv_textcontent_morecommet);
        container_commet = (LinearLayout) findViewById(R.id.container_commet);
        et_textcontent_commet = (EditText) findViewById(R.id.et_textcontent_comment);
        btn_textcontent_submit = (Button) findViewById(R.id.btn_textcontent_submit);
    }

    private void initData() {
        AppContext appContext = AppContext.getInstance();
        requestQueue = appContext.getRequestQueue();
        imageLoader = appContext.getImageLoader();
        Bundle bundle = getIntent().getExtras();
        news_id = bundle.getString("news_id");
        detailUrl = Constants.NEWS_DETAIL + news_id;
        commentUrl = Constants.NEWS_COMMENT + news_id;
        getNetData();
        getComment();
    }

    private void getNetData() {
        StringRequest request = new StringRequest(detailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TextNewsContentActivity.this, "访问数据失败", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    private void showData(String jsonStr) {
        newsDetail = JsonUtils.parseTextNewsDetail(jsonStr);
        if (newsDetail != null) {
            tv_textcontent_time.setText(newsDetail.getCreate_time());
            tv_textcontent_title.setText(newsDetail.getTitle());
            tv_textcontent_content.setText(newsDetail.getContent());
            String cover_pic = newsDetail.getCover_pic();
            netImg.setImageUrl(cover_pic, imageLoader);
            netImg.setDefaultImageResId(R.drawable.feed_focus);
            netImg.setErrorImageResId(R.drawable.feed_focus);
        }
    }

    private void getComment() {
        StringRequest stringRequest = new StringRequest(commentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showComment(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void showComment(String jsonStr) {
        newsComments = JsonUtils.parseNewsComment(jsonStr);
        if (newsComments == null || newsComments.size() == 0) {
            tv_textcontent_morecommet.setVisibility(View.GONE);
            TextView textView = new TextView(this);
            textView.setText("暂无评论，快来抢沙发");
            textView.setTextColor(Color.GRAY);
            container_commet.addView(textView);
        } else if (newsComments.size() > 0) {
            tv_textcontent_morecommet.setVisibility(View.VISIBLE);
            int size = newsComments.size();
            for (int i = 0; i < size; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_list_comment,null);
                TextView comment_time = (TextView) view.findViewById(R.id.item_comment_time);
                TextView comment_user = (TextView) view.findViewById(R.id.item_comment_user);
                TextView comment_content = (TextView) view.findViewById(R.id.item_comment_content);
                NewsComment newsComment = newsComments.get(i);
                comment_time.setText(newsComment.getCreate_time());
                comment_user.setText(newsComment.getUser());
                comment_content.setText(newsComment.getContent());
                container_commet.addView(view);
            }
        }
    }

    private void addListener(){
        img_textcontent_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_textcontent_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_textcontent_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_textcontent_morecommet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextNewsContentActivity.this,MoreCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id",news_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}