package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.king.adapter.CommentListAdapter;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.UserInfo;
import com.king.model.activity.NewsComment;
import com.king.pulltorefresh.PullToRefreshBase;
import com.king.pulltorefresh.PullToRefreshListView;
import com.king.utils.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AUTHOR: King
 * DATE: 2015/4/24.
 */
public class MoreCommentActivity extends Activity {

    private PullToRefreshListView refreshListView;
    private ListView listView;
    private EditText et_morecomment_comment;
    private Button btn_mrecomment_submit;
    private ImageView img_morecomment_back;
    private TextView tv_morecomment_empty;

    private String comentUrl;
    private int currentPage = 1;
    private List<NewsComment> newsComments;
    private CommentListAdapter adapter;

    private RequestQueue requestQueue;
    private AppContext appContext;
    private UserInfo userInfo;
    private String news_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morecomment);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        refreshListView = (PullToRefreshListView) findViewById(R.id.list_morecomment);
        listView = refreshListView.getRefreshableView();
        tv_morecomment_empty = (TextView) findViewById(R.id.tv_morecomment_empty);
        listView.setEmptyView(tv_morecomment_empty);
        et_morecomment_comment = (EditText) findViewById(R.id.et_morecomment_comment);
        btn_mrecomment_submit = (Button) findViewById(R.id.btn_morecomment_submit);
        img_morecomment_back = (ImageView) findViewById(R.id.img_morecomment_back);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        news_id = bundle.getString("news_id");
        comentUrl = Constants.NEWS_COMMENT + news_id + "&p=";
        newsComments = new ArrayList<NewsComment>();
        adapter = new CommentListAdapter(newsComments, this);
        listView.setAdapter(adapter);
        appContext = AppContext.getInstance();
        requestQueue = appContext.getRequestQueue();
        userInfo = appContext.getUserInfo();
        reloadComment();

    }

    private void addListener() {
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                reloadComment();
            }
        });
        refreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                reloadComment();
            }
        });

        btn_mrecomment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_morecomment_comment.getText().toString();
                if (comment.equals("")) {
                    Toast.makeText(MoreCommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (userInfo == null) {
                        Toast.makeText(MoreCommentActivity.this, "登陆后才能发表评论", Toast.LENGTH_SHORT).show();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MoreCommentActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }, 3000);
                    } else {
                        submitComment(comment);
                    }
                }
            }
        });

        img_morecomment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitComment(String comment) {
        String addComment_url = "";
        try {
            comment = URLEncoder.encode(comment, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.ADD_COMMENT);
        sb.append("content=").append(comment);
        sb.append("&news_id=").append(news_id);
        sb.append("&token=").append(userInfo.getToken());
        addComment_url = sb.toString();
        StringRequest stringRequest = new StringRequest(addComment_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String message = JsonUtils.parseMessage(response);
                Toast.makeText(MoreCommentActivity.this, message, Toast.LENGTH_SHORT).show();
                currentPage = 1;
                reloadComment();
                et_morecomment_comment.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MoreCommentActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void reloadComment() {
        StringRequest stringRequest = new StringRequest(comentUrl + currentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (currentPage == 1) {
                    newsComments.clear();
                }
                List<NewsComment> subList = JsonUtils.parseNewsComment(response);
                if (subList != null) {
                    newsComments.addAll(subList);
                    adapter.notifyDataSetChanged();
                }
                refreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = appContext.getUserInfo();
    }

}