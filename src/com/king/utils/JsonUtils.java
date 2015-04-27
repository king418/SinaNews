package com.king.utils;

import com.king.model.*;
import com.king.model.activity.NewsComment;
import com.king.model.activity.TextNewsDetail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Administrator on 2015/4/21.
 */
public class JsonUtils {

    public static List<Title> parseTitleJson(String jsonStr) {
        List<Title> list = new ArrayList<Title>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.getInt("code");
            if (code == 200) {
                JSONArray datas = obj.getJSONArray("data");
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    Title title = new Title();
                    title.setName(data.getString("name"));
                    title.setCate_id(data.getString("cate_id"));
                    list.add(title);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, List<Object>> parseTextNews(String jsonStr) {
        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        List<Object> topNewses = new ArrayList<Object>();
        List<Object> downNewses = new ArrayList<Object>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.getInt("code");
            if (code == 200) {
                JSONObject data = obj.optJSONObject("data");
                JSONArray arrTop_news = data.optJSONArray("top_news");
                int length = arrTop_news.length();
                for (int i = 0; i < length; i++) {
                    JSONObject objTopNews = arrTop_news.optJSONObject(i);
                    TopNews topNews = new TopNews();
                    topNews.setId(objTopNews.optString("id"));
                    topNews.setTitle(objTopNews.optString("title"));
                    topNews.setCover_pic(objTopNews.optString("cover_pic"));
                    topNewses.add(topNews);
                }
                map.put("top_news", topNewses);
                JSONArray arrDown_news = data.optJSONArray("down_news");
                if (arrDown_news != null) {
                    int length1 = arrDown_news.length();
                    for (int i = 0; i < length1; i++) {
                        JSONObject objDownnews = arrDown_news.optJSONObject(i);
                        DownNews downNews = new DownNews();
                        downNews.setId(objDownnews.optString("id"));
                        downNews.setCover_pic(objDownnews.optString("cover_pic"));
                        downNews.setCreate_time(objDownnews.optString("create_time"));
                        downNews.setContent(objDownnews.optString("content"));
                        downNews.setDescript(objDownnews.optString("descript"));
                        downNews.setTitle(objDownnews.optString("title"));
                        downNews.setComment_total(objDownnews.optString("comment_total"));
                        downNewses.add(downNews);
                    }
                }
                map.put("down_news", downNewses);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<ImageNews> parseImageNews(String jsonStr) {
        List<ImageNews> list = new ArrayList<ImageNews>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.getInt("code");
            if (code == 200) {
                JSONArray datas = obj.optJSONArray("data");
                int length = datas.length();
                for (int i = 0; i < length; i++) {
                    JSONObject objData = datas.optJSONObject(i);
                    ImageNews imageNews = new ImageNews();
                    imageNews.setId(objData.optString("id"));
                    imageNews.setTitle(objData.optString("title"));
                    imageNews.setContent(objData.optString("content"));
                    imageNews.setPic_total(objData.optString("pic_total"));
                    imageNews.setDescript(objData.optString("descript"));
                    JSONArray pic_lists = objData.optJSONArray("pic_list");
                    if (pic_lists != null) {
                        List<String> strings = new ArrayList<String>();
                        int length1 = pic_lists.length();
                        for (int j = 0; j < length1; j++) {
                            String s = pic_lists.optString(j);
                            strings.add(s);
                        }
                        imageNews.setPic_list(strings);
                    }
                    list.add(imageNews);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<VideoNews> parseVideoNews(String jsonStr) {
        List<VideoNews> list = new ArrayList<VideoNews>();
        try {
            JSONObject obj = new JSONObject(jsonStr);

            int code = obj.getInt("code");
            if (code == 200) {
                JSONArray datas = obj.optJSONArray("data");
                int length = datas.length();
                for (int i = 0; i < length; i++) {
                    JSONObject data = datas.optJSONObject(i);
                    VideoNews videoNews = new VideoNews();
                    videoNews.setTitle(data.optString("title"));
                    videoNews.setVideo_url(data.optString("video_url"));
                    videoNews.setPlay_num(data.optString("play_num"));
                    videoNews.setPic(data.optString("pic"));
                    list.add(videoNews);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static TextNewsDetail parseTextNewsDetail(String jsonStr) {
        TextNewsDetail newsDetail = new TextNewsDetail();

        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.getInt("code");
            if (code != 200) {
                return null;
            }
            JSONObject data = obj.optJSONObject("data");
            newsDetail.setNews_id(data.optString("news_id"));
            newsDetail.setTitle(data.optString("title"));
            newsDetail.setContent(data.optString("content"));
            newsDetail.setCreate_time(data.optString("create_time"));
            newsDetail.setCover_pic(data.optString("cover_pic"));
            JSONArray pic_lists = data.optJSONArray("pic_list");
            if (pic_lists != null) {
                int length = pic_lists.length();
                List<String> pic_list = new ArrayList<String>();
                for (int i = 0; i < length; i++) {
                    pic_list.add(pic_lists.optString(i));
                }
                newsDetail.setPic_list(pic_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsDetail;
    }

    public static List<NewsComment> parseNewsComment(String jsonStr) {
        List<NewsComment> list = new ArrayList<NewsComment>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.getInt("code");
            if (code != 200) {
                return null;
            }
            JSONArray datas = obj.optJSONArray("data");
            int length = datas.length();
            for (int i = 0; i < length; i++) {
                JSONObject data = datas.optJSONObject(i);
                NewsComment newsComment = new NewsComment();
                newsComment.setContent(data.optString("content"));
                newsComment.setCreate_time(data.optString("create_time"));
                newsComment.setUser(data.optString("user"));
                list.add(newsComment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static UserInfo parseUser(String jsonStr){
        UserInfo userInfo = new UserInfo();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.getInt("code");
            if (code != 200) {
                return null;
            }
            JSONObject data = obj.optJSONObject("data");
            userInfo.setAddress(data.optString("address"));
            userInfo.setBirthday(data.optString("birthday"));
            userInfo.setClient_type(data.optString("client_type"));
            userInfo.setCreate_time(data.optString("create_time"));
            userInfo.setDevice_modle(data.optString("device_model"));
            userInfo.setDevice_token(data.optString("device_token"));
            userInfo.setHeader_img(data.optString("header_img"));
            userInfo.setId(data.optString("id"));
            userInfo.setIp(data.optString("ip"));
            userInfo.setJid(data.optString("jid"));
            userInfo.setLast_login_time(data.optString("last_login_time"));
            userInfo.setLbs_lat(data.optString("lbs_lat"));
            userInfo.setLbs_long(data.optString("lbs_long"));
            userInfo.setNickname(data.optString("nickname"));
            userInfo.setOs_version(data.optString("os_version"));
            userInfo.setSex(data.optString("sex"));
            userInfo.setStatus(data.optString("status"));
            userInfo.setToken(data.optString("token"));
            userInfo.setUsername(data.optString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    public static String parseMessage(String jsonStr){
        String message = null;
        try {
            JSONObject obj = new JSONObject(jsonStr);
            message = obj.optString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static List<MyComment> parseMyComment(String jsonStr){
        List<MyComment> list = new ArrayList<MyComment>();

        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.optInt("code");
            if (code!=200){
                return null;
            }
            JSONArray datas = obj.optJSONArray("data");
            int length = datas.length();
            for (int i = 0; i < length; i++) {
                JSONObject data = datas.optJSONObject(i);
                MyComment myComment = new MyComment();
                myComment.setCreate_time(data.optString("create_time"));
                myComment.setTitle(data.optString("title"));
                myComment.setContent(data.optString("content"));
                list.add(myComment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
