package com.coinlift.cryptoservice.controller;

import com.coinlift.cryptoservice.pojo.CryptoData;
import com.coinlift.cryptoservice.pojo.CryptoEvent;
import com.coinlift.cryptoservice.pojo.CryptoPercentData;
import com.coinlift.cryptoservice.pojo.CryptocurrencyNews;
import com.coinlift.cryptoservice.service.CryptoEventsService;
import com.coinlift.cryptoservice.service.CryptoDataService;
import com.coinlift.cryptoservice.service.CryptocurrencyNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cryptocurrency")
@CrossOrigin("*")
public class CryptoController {
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


