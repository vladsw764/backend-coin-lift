package com.coinlift.backend.pojo;

import lombok.Builder;

@Builder
public class CryptocurrencyNews {

    private String title;

    private String url;

    private String description;

    private String thumbnail;

    private String createdAt;

    public CryptocurrencyNews(String title, String url, String description, String thumbnail, String createdAt) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
