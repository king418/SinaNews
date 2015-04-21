package com.king.SinaNews;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.Title;
import com.king.utils.JsonUtils;

import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private LinearLayout tab_main;
    private ImageView sliding_control;
    private HorizontalScrollView scrollView;
    private LinearLayout title_container;
    private LinearLayout title_navigat;
    /**
     * 导航栏标题控件数组
     */
    private TextView[] title_navigation;
    /**
     * 导航栏标题数组
     */
    private String[] title_name;
    /**
     * 每个Fragment的导航栏地址
     */
    private String[] title_type;

    /**
     * 当前在第几个Fragment
     */
    private int currentFragment;
    private RequestQueue requestQueue;
    private List<Title> title_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initData();
        initView();
        initListener();

    }

    private void initView() {
        tab_main = (LinearLayout) findViewById(R.id.tab_main);
        sliding_control = (ImageView) findViewById(R.id.sliding_control);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        title_container = (LinearLayout) findViewById(R.id.title_container);
        title_navigat = (LinearLayout) findViewById(R.id.title_nagivation);
        setTitle();
    }

    private void initListener() {
        final int childCount = tab_main.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final LinearLayout tab = (LinearLayout) tab_main.getChildAt(i);
            final int index = i;
            if (i == 0) {
                setStateDisEnable(tab);
            }
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentFragment = index;
                    title_container.removeAllViews();
                    setTitle();
                    setStateDisEnable(tab);
                    for (int j = 0; j < childCount; j++) {
                        if (j != index) {
                            LinearLayout tabs = (LinearLayout) tab_main.getChildAt(j);
                            setStateEnable(tabs);
                        }
                    }
                }
            });
        }
    }

    private void initData() {
        title_type = new String[]{Constants.NAVIGATION_TYPE_TEXT, Constants.NAVIGATION_TYPE_PICTURE, Constants.NAVIGATION_TYPE_VIDEO};
        requestQueue = AppContext.getInstance().getRequestQueue();
    }

    /**
     * 下方ActionBar选中 UI
     */
    private void setStateDisEnable(LinearLayout tab) {
        if (tab != null) {
            tab.setEnabled(false);
            tab.getChildAt(0).setEnabled(false);
            TextView childAt = (TextView) tab.getChildAt(1);
            childAt.setTextColor(Color.rgb(0xF5, 0X4B, 0X4C));
        }
    }

    /**
     * 下方ActionBar未选中 UI
     *
     * @param tab
     */
    private void setStateEnable(LinearLayout tab) {
        if (tab != null) {
            tab.setEnabled(true);
            tab.getChildAt(0).setEnabled(true);
            TextView childAt = (TextView) tab.getChildAt(1);
            childAt.setTextColor(Color.BLACK);
        }
    }

    private String getTitleUrl(String type) {
        return Constants.NEWS_NAVIGATION + type;
    }

    private void setTitle() {
        String titleUrl = "";
        if (currentFragment < 3) {
            title_navigat.setVisibility(View.VISIBLE);
            titleUrl = getTitleUrl(title_type[currentFragment]);
            StringRequest stringRequest = new StringRequest(titleUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null && !response.equals("")) {
                        title_list = JsonUtils.parseTitleJson(response);
                        initTitle(title_list);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(stringRequest);
        } else {
            title_navigat.setVisibility(View.GONE);
        }
    }


    private void initTitle(List<Title> list) {
        if (list != null) {
            title_navigation = new TextView[list.size()];
            final int length = title_navigation.length;
            for (int i = 0; i < length; i++) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText(title_list.get(i).getName());
                textView.setTextSize(17);
                textView.setTextColor(Color.BLACK);
                textView.setPadding(15, 20, 15, 15);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                title_navigation[i] = textView;
                final int index = i;
                title_navigation[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollView.scrollTo(index * 60, 0);
                        title_navigation[index].setBackgroundResource(R.drawable.title_background);
                        title_navigation[index].setTextColor(Color.rgb(0xF5, 0X4B, 0X4C));
                        for (int j = 0; j < length; j++) {
                            if (j != index) {
                                title_navigation[j].setBackgroundColor(Color.argb(0, 0, 0, 0));
                                title_navigation[j].setTextColor(Color.BLACK);
                            }
                        }
                    }
                });
                title_container.addView(textView);
            }
            title_navigation[0].setBackgroundResource(R.drawable.title_background);
            title_navigation[0].setTextColor(Color.rgb(0xF5, 0X4B, 0X4C));
        }
    }

}
