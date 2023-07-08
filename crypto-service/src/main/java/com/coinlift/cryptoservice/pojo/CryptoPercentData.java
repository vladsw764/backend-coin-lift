package com.coinlift.cryptoservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CryptoPercentData {
    private String name;
    private double percentChange24h;
    private String imageUrl;
}
