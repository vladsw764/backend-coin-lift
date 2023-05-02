package com.coinlift.backend.controllers;

import com.coinlift.backend.pojo.CryptoData;
import com.coinlift.backend.pojo.CryptoEvent;
import com.coinlift.backend.pojo.CryptoPercentData;
import com.coinlift.backend.pojo.CryptocurrencyNews;
import com.coinlift.backend.services.apis.CryptoEventsService;
import com.coinlift.backend.services.apis.CryptoDataService;
import com.coinlift.backend.services.apis.CryptocurrencyNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cryptocurrency")
public class CryptocurrencyController {
    private final CryptoDataService dataService;
    private final CryptocurrencyNewsService newsService;
    private final CryptoEventsService eventsService;

    @GetMapping("/percent-data")
    public ResponseEntity<List<CryptoPercentData>> getCryptoPercentData() {
        return ResponseEntity.ok(dataService.getCryptoPercentData());
    }

    @GetMapping("/all-data")
    public ResponseEntity<List<CryptoData>> getCryptoData() {
        return ResponseEntity.ok(dataService.getAllCryptoData());
    }

    @GetMapping("/news")
    public ResponseEntity<List<CryptocurrencyNews>> getCryptocurrencyNews() {
        return ResponseEntity.ok(newsService.getCoinDeskArticles());
    }

    @GetMapping("/events")
    public ResponseEntity<List<CryptoEvent>> getCryptoEvent() {
        return ResponseEntity.ok(eventsService.getLatestCryptoEvents());
    }
}
