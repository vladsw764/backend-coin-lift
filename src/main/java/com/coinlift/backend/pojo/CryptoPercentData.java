package com.coinlift.backend.pojo;

public class CryptoPercentData {

    private String name;

    private double percentChange24h;

    private String imageUrl;

    public CryptoPercentData(String name, double percentChange24h, String imageUrl) {
        this.name = name;
        this.percentChange24h = percentChange24h;
        this.imageUrl = imageUrl;
    }

    public CryptoPercentData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
