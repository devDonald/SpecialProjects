package com.godlife.churchapp.godlifeassembly.models;

public class EventsModel {
    private String id, title, details, noticeDate, image;

    public EventsModel() {
    }

    public EventsModel(String id, String title, String details, String noticeDate, String image) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.noticeDate = noticeDate;
        this.image = image;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
