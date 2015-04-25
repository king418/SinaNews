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

}
