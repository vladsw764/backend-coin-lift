package com.coinlift.backend.controllers;

import com.coinlift.backend.pojo.CryptoPercentData;
import com.coinlift.backend.pojo.CryptocurrencyNews;
import com.coinlift.backend.services.apis.CryptoPercentDataService;
import com.coinlift.backend.services.apis.CryptocurrencyNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cryptocurrency")
public class CryptocurrencyController {
    private final CryptoPercentDataService dataService;
    private final CryptocurrencyNewsService newsService;

    @GetMapping("/percent-data")
    public List<CryptoPercentData> getCryptoPercentData() {
        return dataService.getCryptoPercentData();
    }

    @GetMapping("/news")
    public List<CryptocurrencyNews> getCryptocurrencyNews() {
        return newsService.getCoinDeskArticles();
    }
}
