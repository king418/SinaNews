package com.king.SinaNews;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * AUTHOR: King
 * DATE: 2015/4/24.
 */
public class VideoDetailActivity extends Activity {

    private VideoView videoView_play;
    private ImageView img_play;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetail);

        videoView_play = (VideoView) findViewById(R.id.videoView_playvideo);
        img_play = (ImageView) findViewById(R.id.img_play);
        Bundle bundle = getIntent().getExtras();
        String video_url = bundle.getString("video_url");
        if (video_url.equals("false")){
            Toast.makeText(this,"没有相关视频",Toast.LENGTH_SHORT).show();
        }else {
            img_play.setVisibility(View.GONE);
            videoView_play.setVideoURI(Uri.parse(video_url));
        }
    }
}