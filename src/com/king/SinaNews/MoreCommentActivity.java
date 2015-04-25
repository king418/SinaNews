package com.king.SinaNews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.king.adapter.CommentListAdapter;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.activity.NewsComment;
import com.king.pulltorefresh.PullToRefreshBase;
import com.king.pulltorefresh.PullToRefreshListView;
import com.king.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

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

    private String comentUrl;
    private int currentPage = 1;
    private List<NewsComment> newsComments;
    private CommentListAdapter adapter;

    private RequestQueue requestQueue;

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
        et_morecomment_comment = (EditText) findViewById(R.id.et_morecomment_comment);
        btn_mrecomment_submit = (Button) findViewById(R.id.btn_morecomment_submit);
        img_morecomment_back = (ImageView) findViewById(R.id.img_morecomment_back);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String news_id = bundle.getString("news_id");
        comentUrl = Constants.NEWS_COMMENT + news_id + "&p=";
        newsComments = new ArrayList<NewsComment>();
        adapter = new CommentListAdapter(newsComments,this);
        listView.setAdapter(adapter);
        requestQueue = AppContext.getInstance().getRequestQueue();
        reloadComment();

    }

    private void addListener(){
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

            }
        });

        img_morecomment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void reloadComment(){
        StringRequest stringRequest = new StringRequest(comentUrl + currentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (currentPage == 1){
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

}