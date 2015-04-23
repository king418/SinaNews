package com.king.model;

import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/22.
 */
public class ImageNews {
    private String id;
    private String title;
    private String content;
    private String pic_total;
    private List<String> pic_list;
    private String descript;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic_total() {
        return pic_total;
    }

    public void setPic_total(String pic_total) {
        this.pic_total = pic_total;
    }

    public List<String> getPic_list() {
        return pic_list;
    }

    public void setPic_list(List<String> pic_list) {
        this.pic_list = pic_list;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
