package com.coinlift.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CryptocurrencyNews {
    private String title;
    private String url;
    private String description;
    private String thumbnail;
    private String createdAt;
}
