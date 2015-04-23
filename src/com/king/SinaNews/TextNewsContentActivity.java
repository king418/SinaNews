package com.king.SinaNews;

import android.app.Activity;
import android.os.Bundle;
import com.android.volley.toolbox.NetworkImageView;
import com.king.model.activity.TextNewsDetail;

/**
 * AUTHOR: King
 * DATE: 2015/4/23.
 */
public class  TextNewsContentActivity extends Activity {
    private NetworkImageView netImg;
    private TextNewsDetail newsDetail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textnewscontent);
    }
}