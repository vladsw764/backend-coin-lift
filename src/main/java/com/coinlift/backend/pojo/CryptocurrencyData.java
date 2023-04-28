package com.coinlift.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CryptocurrencyData {
    private String name;
    private double percentChange24h;
}
