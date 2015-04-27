package com.king.SinaNews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
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
import com.king.utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/24.
 */
public class ImageDetailActivity extends Activity {

    //  private Button btn_imgDetail_back;
    //   private Button btn_imgDetail_share;
      private Button btn_imgDetail_favorite;

    private ViewPager vp_imgDetail_pic;
    private TextView tv_imgDetail_title;
    private TextView tv_imgDetail_page;
    private TextView tv_imgDetail_content;
    private ProgressDialog dialog;
    private LinearLayout linearLayout_picdetail_comments;

    private String imgDetailUrl;
    private RequestQueue requestQueue;
    private MySQLiteOpenHelper dbHelper;
    private ImageLoader imageLoader;
    private List<View> pager;
    private HeadPagerAdapter adapter;
    private TextNewsDetail newsDetail;
    private String news_id;
    private boolean isCollect;
    private boolean islogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedetail);
        initView();
        initData();
        addListener();
    }

    private void initView() {
         //btn_imgDetail_back = (Button) findViewById(R.id.btn_picdetail_back);
        btn_imgDetail_favorite = (Button) findViewById(R.id.btn_picdetail_favorite);
        btn_imgDetail_favorite.setTag(0);
        // btn_imgDetail_share = (Button) findViewById(R.id.btn_picdetail_share);
        linearLayout_picdetail_comments = (LinearLayout) findViewById(R.id.linearLayout_picdetail_comments);
        vp_imgDetail_pic = (ViewPager) findViewById(R.id.viewPager_picdetail_pic);
        tv_imgDetail_content = (TextView) findViewById(R.id.textView_picdetail_content);
        tv_imgDetail_page = (TextView) findViewById(R.id.textView_picdetail_page);
        tv_imgDetail_title = (TextView) findViewById(R.id.textView_picdetail_title);
        dialog = new ProgressDialog(this);
        dialog.setMessage("图片加载中。。。");
    }

    private void initData() {
        dbHelper = new MySQLiteOpenHelper(this);
        Bundle bundle = getIntent().getExtras();
        news_id = bundle.getString("news_id");
        imgDetailUrl = Constants.NEWS_DETAIL + news_id;
        requestQueue = AppContext.getInstance().getRequestQueue();
        imageLoader = AppContext.getInstance().getImageLoader();
        pager = new ArrayList<View>();
        islogin = AppContext.getInstance().isLogin();
        if (islogin){
            initFavorite();
        }
        shareData();
    }

    private void shareData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(imgDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newsDetail = JsonUtils.parseTextNewsDetail(response);
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
                if (islogin){
                    setFavorite();
                }else {
                    Toast.makeText(this,"登陆后才能收藏",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_picdetail_share:
                showShare();
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
        linearLayout_picdetail_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageDetailActivity.this, MoreCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id", news_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initFavorite() {
        int count = dbHelper.selectCount("select * from tb_collect where news_id=?", new String[]{news_id});
        if (count > 0) {
            isCollect = true;
        }
        if (isCollect){
            btn_imgDetail_favorite.setBackgroundResource(R.drawable.img_favorited);
            btn_imgDetail_favorite.setTag(1);
        }else {
            btn_imgDetail_favorite.setBackgroundResource(R.drawable.img_favorite);
            btn_imgDetail_favorite.setTag(0);
        }
    }

    private void setFavorite(){
        int tag = (Integer) btn_imgDetail_favorite.getTag();
        switch(tag){
            case 0:
                boolean seccess = dbHelper.execData("insert into tb_collect (news_id,title,content) values (?,?,?)",
                        new String[]{newsDetail.getNews_id(), newsDetail.getTitle(), newsDetail.getContent()});
                if (seccess){
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    btn_imgDetail_favorite.setBackgroundResource(R.drawable.img_favorited);
                    btn_imgDetail_favorite.setTag(1);
                }else {
                    Toast.makeText(this,"收藏失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                boolean seccessed = dbHelper.execData("delete from tb_collect where news_id = ?",new String[]{news_id});
                if (seccessed){
                    Toast.makeText(this,"取消收藏",Toast.LENGTH_SHORT).show();
                    btn_imgDetail_favorite.setBackgroundResource(R.drawable.img_favorite);
                    btn_imgDetail_favorite.setTag(0);
                }else {
                    Toast.makeText(this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                }
                break;
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
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}