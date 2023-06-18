package com.coinlift.backend.pojo;

import lombok.*;

@AllArgsConstructor
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
}
