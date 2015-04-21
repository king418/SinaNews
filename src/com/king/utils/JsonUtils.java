package com.king.utils;

import com.king.model.DownNews;
import com.king.model.Title;
import com.king.model.TopNews;
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
            JSONArray datas = obj.getJSONArray("data");
            for (int i = 0; i < datas.length(); i++) {
                JSONObject data = datas.getJSONObject(i);
                Title title = new Title();
                title.setName(data.getString("name"));
                title.setCate_id(data.getString("cate_id"));
                list.add(title);
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
            JSONObject data = obj.getJSONObject("data");
            JSONArray arrTop_news = data.getJSONArray("top_news");
            int length = arrTop_news.length();
            for (int i = 0; i < length; i++) {
                JSONObject objTopNews = arrTop_news.getJSONObject(i);
                TopNews topNews = new TopNews();
                topNews.setId(objTopNews.getString("id"));
                topNews.setTitle(objTopNews.getString("title"));
                topNews.setCover_pic(objTopNews.getString("cover_pic"));
                topNewses.add(topNews);
            }
            map.put("top_news", topNewses);
            JSONArray arrDown_news = data.getJSONArray("down_news");
            int length1 = arrDown_news.length();
            for (int i = 0; i < length1; i++) {
                JSONObject objDownnews = arrDown_news.getJSONObject(i);
                DownNews downNews = new DownNews();
                downNews.setId(objDownnews.getString("id"));
                downNews.setCover_pic(objDownnews.getString("cover_pic"));
                downNews.setCreate_time(objDownnews.getString("create_time"));
                downNews.setContent(objDownnews.getString("content"));
                downNews.setDescript(objDownnews.getString("descript"));
                downNews.setTitle(objDownnews.getString("title"));
                downNews.setContent(objDownnews.getString("comment_total"));
                downNewses.add(downNews);
            }
            map.put("down_news", downNewses);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

}
