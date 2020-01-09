package com.godlife.churchapp.godlifeassembly.models;

import java.util.HashMap;
import java.util.Map;

public class PraiseModel {
    private String id, title, content, author, praiseDate, praiseTime;

    public PraiseModel() {
    }


    public PraiseModel(String id, String title, String content, String author, String praiseDate, String praiseTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.praiseDate = praiseDate;
        this.praiseTime = praiseTime;
    }

    public PraiseModel(String title, String content, String author, String praiseDate, String praiseTime) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.praiseDate = praiseDate;
        this.praiseTime = praiseTime;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPraiseDate() {
        return praiseDate;
    }

    public void setPraiseDate(String praiseDate) {
        this.praiseDate = praiseDate;
    }

    public String getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(String praiseTime) {
        this.praiseTime = praiseTime;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("title", this.title);
        result.put("content", this.content);
        result.put("author",this.author);
        result.put("praiseDate",this.praiseDate);
        result.put("praiseTime",this.praiseTime);

        return result;
    }
}
