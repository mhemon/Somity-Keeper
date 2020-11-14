package com.somitykeeper.app;

public class NotificationModel {
    private String title;
    private String description;
    private String img_url;
    private String date;

    public NotificationModel(){
        //for firebase
    }

    public NotificationModel(String title, String description, String img_url, String date) {
        this.title = title;
        this.description = description;
        this.img_url = img_url;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
