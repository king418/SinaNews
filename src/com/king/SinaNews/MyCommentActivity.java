package com.king.SinaNews;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.king.adapter.MyCommentAdapter;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.MyComment;
import com.king.model.UserInfo;
import com.king.pulltorefresh.PullToRefreshBase;
import com.king.pulltorefresh.PullToRefreshListView;
import com.king.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AUTHOR: King
 * DATE: 2015/4/27.
 */
public class MyCommentActivity extends Activity {

    private ImageView img_mycomment_back;
    private ListView list_mycomment;
    private List<MyComment> myComments;
    private PullToRefreshListView list;

    private RequestQueue requestQueue;
    private UserInfo userInfo;
    private int currentPage = 1;
    private AppContext appContext;
    private String token;
    private boolean islogin;
    private MyCommentAdapter adapter;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomment);
        initView();
        initData();
        addLisetener();
    }

    private void initView() {
        list = (PullToRefreshListView) findViewById(R.id.list_mycomment);
        list_mycomment = list.getRefreshableView();
        img_mycomment_back = (ImageView) findViewById(R.id.img_mycomment_back);
    }

    private void initData() {
        appContext = AppContext.getInstance();
        requestQueue = appContext.getRequestQueue();
        islogin = appContext.isLogin();
        if (islogin) {
            userInfo = appContext.getUserInfo();
            token = userInfo.getToken();
        }
        myComments = new ArrayList<MyComment>();
        adapter = new MyCommentAdapter(this, myComments);
        list_mycomment.setAdapter(adapter);
        url = Constants.MY_COMMENT + "?token=" + token + "&p=";
        reloadListView();
    }

    private void addLisetener() {
        img_mycomment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                reloadListView();
            }
        });
        list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                Toast.makeText(appContext,"正在加载",Toast.LENGTH_SHORT).show();
                reloadListView();
            }
        });
    }


    private void reloadListView() {
        StringRequest stringRequest = new StringRequest(url + currentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                List<MyComment> myComments1 = JsonUtils.parseMyComment(response);

                if (myComments1 != null) {
                    if (currentPage == 1) {
                        myComments.clear();
                    }
                    myComments.addAll(myComments1);
                    adapter.notifyDataSetChanged();
                }
                list.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("token", token);
//                map.put("p", currentPage + "");
//                return super.getParams();
//            }
//        };
        requestQueue.add(stringRequest);
    }

}