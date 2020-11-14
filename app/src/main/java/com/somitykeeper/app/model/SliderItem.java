package com.somitykeeper.app.model;

public class SliderItem {
    private String description;
    private String imageUrl;
    private String destinationurl;

    public SliderItem() {
        //for firebase
    }

    public SliderItem(String description, String imageUrl, String destinationurl) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.destinationurl = destinationurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDestinationurl() {
        return destinationurl;
    }

    public void setDestinationurl(String destinationurl) {
        this.destinationurl = destinationurl;
    }
}
