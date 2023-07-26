package com.coinlift.backend.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoPercentData {

    private String name;

    private double percentChange24h;

    private String imageUrl;

    public CryptoPercentData(String name, double percentChange24h, String imageUrl) {
        this.name = name;
        this.percentChange24h = percentChange24h;
        this.imageUrl = imageUrl;
    }
}
