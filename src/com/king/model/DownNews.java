package com.king.model;

/**
 * Created by Administrator on 2015/4/21.
 */
public class DownNews {

    private String id;
    private String title;
    private String content;
    private String create_time;
    private String cover_pic;
    private String descript;
    private String comment_total;

    public String getComment_total() {
        return comment_total;
    }

    public void setComment_total(String comment_total) {
        this.comment_total = comment_total;
    }

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
