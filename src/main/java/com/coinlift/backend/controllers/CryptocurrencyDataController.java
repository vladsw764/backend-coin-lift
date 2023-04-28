package com.coinlift.backend.controllers;

import com.coinlift.backend.pojo.CryptocurrencyData;
import com.coinlift.backend.services.CryptocurrencyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cryptocurrency")
public class CryptocurrencyDataController {

    private final CryptocurrencyDataService dataService;

    @GetMapping()
    public List<CryptocurrencyData> getCryptocurrencyData() throws IOException, URISyntaxException {
        return dataService.getCryptocurrencyData();
    }
}
