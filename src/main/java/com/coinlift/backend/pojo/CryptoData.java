package com.coinlift.backend.pojo;

import lombok.Builder;

@Builder
public class CryptoData {

    private String imageUrl;

    private String name;

    private String symbol;

    private double price;

    private double percentChange1H;

    private double percentChange1D;

    private double percentChange1W;

    private double marketCap;

    private long volumeUSD;

    private long circulatingSupply;

    public CryptoData(String imageUrl, String name, String symbol, double price, double percentChange1H, double percentChange1D, double percentChange1W, double marketCap, long volumeUSD, long circulatingSupply) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.percentChange1H = percentChange1H;
        this.percentChange1D = percentChange1D;
        this.percentChange1W = percentChange1W;
        this.marketCap = marketCap;
        this.volumeUSD = volumeUSD;
        this.circulatingSupply = circulatingSupply;
    }

    public CryptoData() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercentChange1H() {
        return percentChange1H;
    }

    public void setPercentChange1H(double percentChange1H) {
        this.percentChange1H = percentChange1H;
    }

    public double getPercentChange1D() {
        return percentChange1D;
    }

    public void setPercentChange1D(double percentChange1D) {
        this.percentChange1D = percentChange1D;
    }

    public double getPercentChange1W() {
        return percentChange1W;
    }

    public void setPercentChange1W(double percentChange1W) {
        this.percentChange1W = percentChange1W;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public long getVolumeUSD() {
        return volumeUSD;
    }

    public void setVolumeUSD(long volumeUSD) {
        this.volumeUSD = volumeUSD;
    }

    public long getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(long circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }
}
