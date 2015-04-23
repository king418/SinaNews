package com.king.utils;

import com.king.model.DownNews;
import com.king.model.ImageNews;
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
            map.put("down_news", downNewses);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<ImageNews> parseImageNews(String jsonStr) {
        List<ImageNews> list = new ArrayList<ImageNews>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray datas = obj.getJSONArray("data");
            int length = datas.length();
            for (int i = 0; i < length; i++) {
                JSONObject objData = datas.getJSONObject(i);
                ImageNews imageNews = new ImageNews();
                imageNews.setId(objData.optString("id"));
                imageNews.setTitle(objData.optString("title"));
                imageNews.setContent(objData.optString("content"));
                imageNews.setPic_total(objData.optString("pic_total"));
                imageNews.setDescript(objData.optString("descript"));
                JSONArray pic_lists = objData.optJSONArray("pic_list");
                if (pic_lists!=null){
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
