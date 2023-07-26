package com.coinlift.backend.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
