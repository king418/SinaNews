package com.king.configuration;

/**
 * Created by Administrator on 2015/4/21.
 */
public class Constants {
    /**
     * 导航栏信息
     */
    public static final String NEWS_NAVIGATION =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/cate_list?type=";
    /**
     * 导航栏分类
     */
    public static final String NAVIGATION_TYPE_TEXT = "text";
    public static final String NAVIGATION_TYPE_PICTURE = "pic";
    public static final String NAVIGATION_TYPE_VIDEO = "video";

    /**
     * 文字新闻列表地址
     */
    public static final String NEWS_LIST =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/get_news_list?cate_id=";
    /**
     * 图片新闻地址
     */
    public static final String PIC_NEWS_LIST =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/get_pic_news_list?cate_id=";
    /**
     * 视频新闻地址
     */
    public static final String VIDEO_NEWS_LIST =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/get_video_news_list?cate_id=";
    /**
     * 文字新闻详细
     */
    public static final String NEWS_DETAIL =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/news_detail?news_id=";
    /**
     * 新闻评论
     */
    public static final String NEWS_COMMENT =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/get_comment_list?item_id=";
    /**
     * 登陆url
     */
    public static final String DO_LOGIN =
            "http://1000phone.net:8088/qfapp/index.php/juba/index/do_login";
    /**
     * 获取图片验证码url
     */
    public static final String CODE_URL =
            "http://1000phone.net:8088/qfapp/index.php/juba/index/verify_code?sequence=";
    /**
     * 注册url
     */
    public static final String DO_REGISTER =
            "http://1000phone.net:8088/qfapp/index.php/juba/index/do_register";
    /**
     * 评论接口
     */
    public static final String ADD_COMMENT =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/add_comment?";
    /**
     * 我的评论
     */
    public static final String MY_COMMENT =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/get_my_comment";
    /**
     * 发表文字新闻
     */
    public static final String ADD_NEWS =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/add_news";
    /**
     * 发表图片新闻
     */
    public static final String ADD_IMG_NEWS =
            "http://1000phone.net:8088/qfapp/index.php/juba/news/add_pic_news";
}
