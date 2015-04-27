package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.UserInfo;
import com.king.model.activity.NewsComment;
import com.king.model.activity.TextNewsDetail;
import com.king.utils.JsonUtils;
import com.king.utils.MySQLiteOpenHelper;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AUTHOR: King
 * DATE: 2015/4/23.
 */
public class TextNewsContentActivity extends Activity {
    private NetworkImageView netImg;
    private TextNewsDetail newsDetail;
    private List<NewsComment> newsComments;
    private UserInfo userInfo;

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
    private AppContext appContext;
    private MySQLiteOpenHelper dbHelper;
    private boolean isCollect;

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
        img_textcontent_favorite.setTag(0);
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
        dbHelper = new MySQLiteOpenHelper(this);
        appContext = AppContext.getInstance();
        requestQueue = appContext.getRequestQueue();
        imageLoader = appContext.getImageLoader();
        Bundle bundle = getIntent().getExtras();
        news_id = bundle.getString("news_id");
        detailUrl = Constants.NEWS_DETAIL + news_id;
        commentUrl = Constants.NEWS_COMMENT + news_id;
        userInfo = appContext.getUserInfo();
        if (userInfo != null) {
            initFavorite();
        }

        getNetData();
        getComment();
    }

    private void getNetData() {
        StringRequest request = new StringRequest(detailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("---->", "----->" + response);
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
            if (!cover_pic.equals("null")) {
                netImg.setImageUrl(cover_pic, imageLoader);
                netImg.setDefaultImageResId(R.drawable.feed_focus);
                netImg.setErrorImageResId(R.drawable.feed_focus);
            }
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
                Toast.makeText(TextNewsContentActivity.this, "拉取评论失败", Toast.LENGTH_SHORT).show();
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
            container_commet.removeAllViews();
            int size = newsComments.size();
            for (int i = 0; i < size; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_list_comment, null);
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

    private void addListener() {
        img_textcontent_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_textcontent_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        img_textcontent_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavorite();
            }
        });

        tv_textcontent_morecommet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextNewsContentActivity.this, MoreCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id", news_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_textcontent_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_textcontent_commet.getText().toString();
                if (comment.equals("")) {
                    Toast.makeText(TextNewsContentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (userInfo == null) {
                        Toast.makeText(TextNewsContentActivity.this, "登陆后才能发表评论", Toast.LENGTH_SHORT).show();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TextNewsContentActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }, 3000);
                    } else {
                        submitComment(comment);
                    }
                }
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
                Toast.makeText(TextNewsContentActivity.this, message, Toast.LENGTH_SHORT).show();
                getNetData();
                getComment();
                et_textcontent_commet.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TextNewsContentActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void initFavorite() {
        int count = dbHelper.selectCount("select * from tb_collect where news_id=?", new String[]{news_id});
        if (count > 0) {
            isCollect = true;
        }
        if (isCollect){
            img_textcontent_favorite.setImageResource(R.drawable.favorited_normal);
            img_textcontent_favorite.setTag(1);
        }else {
            img_textcontent_favorite.setImageResource(R.drawable.favorite_normal);
            img_textcontent_favorite.setTag(0);
        }
    }

    private void setFavorite(){
        int tag = (Integer) img_textcontent_favorite.getTag();
        switch(tag){
            case 0:
                boolean seccess = dbHelper.execData("insert into tb_collect (news_id,title,content) values (?,?,?)",
                        new String[]{newsDetail.getNews_id(), newsDetail.getTitle(), newsDetail.getContent()});
                if (seccess){
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                    img_textcontent_favorite.setImageResource(R.drawable.favorited_normal);
                    img_textcontent_favorite.setTag(1);
                }else {
                    Toast.makeText(this,"收藏失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                boolean seccessed = dbHelper.execData("delete from tb_collect where news_id = ?",new String[]{news_id});
                if (seccessed){
                    Toast.makeText(this,"取消收藏",Toast.LENGTH_SHORT).show();
                    img_textcontent_favorite.setImageResource(R.drawable.favorite_normal);
                    img_textcontent_favorite.setTag(0);
                }else {
                    Toast.makeText(this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = appContext.getUserInfo();
        if (userInfo != null) {
            initFavorite();
        }else {
            img_textcontent_favorite.setImageResource(R.drawable.favorite_normal);
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(newsDetail.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(newsDetail.getContent());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        //siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}