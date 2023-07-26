package com.coinlift.backend.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
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
}
