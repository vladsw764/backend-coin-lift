package com.coinlift.backend.services.apis;

import com.coinlift.backend.entities.CryptoImage;
import com.coinlift.backend.pojo.CryptoEvent;
import com.coinlift.backend.repositories.CryptoImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CryptoEventsService {
    private final CryptoImageRepository cryptoImageRepository;
    private static final String API_BASE_URL = "https://api.coinpaprika.com/v1/coins/";
    private static final String API_EVENT_ENDPOINT = "/events";
    List<String> topCryptos = Arrays.asList("btc-bitcoin", "eth-ethereum",
            "bnb-binance-coin", "ada-cardano", "xrp-xrp");

    public List<CryptoEvent> getLatestCryptoEvents() {
        RestTemplate restTemplate = new RestTemplate();
        List<CryptoEvent> events = new ArrayList<>();

        for (String crypto : topCryptos) {
            String apiUrl = API_BASE_URL + crypto + API_EVENT_ENDPOINT;
            CryptoEvent[] cryptoEvents = restTemplate.getForObject(apiUrl, CryptoEvent[].class);

            for (CryptoEvent event : cryptoEvents != null ? cryptoEvents : new CryptoEvent[0]) {
                String result = crypto.substring(crypto.indexOf('-') + 1);

                LocalDateTime eventDate = LocalDateTime.parse(event.getDate(), DateTimeFormatter.ISO_DATE_TIME);
                if (eventDate.isAfter(LocalDateTime.now().minusDays(1))) {
                    ZonedDateTime zonedEventDate = ZonedDateTime.parse(event.getDate()).withZoneSameInstant(ZoneId.systemDefault());
                    event.setDate(zonedEventDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    event.setCryptoName(result.toUpperCase());

                    // Get the CryptoImage object from the repository based on cryptoId
                    CryptoImage cryptoImage = cryptoImageRepository.findCryptoImageByCryptoName(crypto);
                    if (cryptoImage != null) {
                        event.setImageLink(cryptoImage.getCryptoImageUrl());
                    } else {
                        event.setImageLink(""); // Set a default image link or handle the case when no image is found
                    }

                    events.add(event);
                }
            }
        }
        return events;
    }
}
