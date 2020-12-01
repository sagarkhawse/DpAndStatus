package com.status.tdsmo.models;

public class Image {
    private String image;
    private int id;
    private String title;
    private String language;
    private String status;
    private String category;
    private String url;
    private String banner;
    private String interstitial;
    private int viewType = 1;


    public Image(String status) {
        this.status = status;
    }

    public Image setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
    public int getViewType() {
        return viewType;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;

    }


    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getBanner() {
        return banner;
    }

    public String getInterstitial() {
        return interstitial;
    }
}
