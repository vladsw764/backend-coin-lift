package com.coinlift.backend.services.apis;

import com.coinlift.backend.pojo.CryptoEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CryptoEventsService {
    private static final String API_BASE_URL = "https://api.coinpaprika.com/v1/coins/";
    private static final String API_EVENT_ENDPOINT = "/events";
    List<String> topCryptos = Arrays.asList("btc-bitcoin", "eth-ethereum",
            "bnb-binance-coin", "ada-cardano", "xrp-xrp", "doge-dogecoin",
            "dot-polkadot", "uni-uniswap", "bch-bitcoin-cash", "ltc-litecoin");

    public List<CryptoEvent> getLatestCryptoEvents() {
        RestTemplate restTemplate = new RestTemplate();
        List<CryptoEvent> events = new ArrayList<>();

        for (String crypto : topCryptos) {
            String apiUrl = API_BASE_URL + crypto + API_EVENT_ENDPOINT;
            CryptoEvent[] cryptoEvents = restTemplate.getForObject(apiUrl, CryptoEvent[].class);
            for (CryptoEvent event : cryptoEvents != null ? cryptoEvents : new CryptoEvent[0]) {
                LocalDateTime eventDate = LocalDateTime.parse(event.getDate(), DateTimeFormatter.ISO_DATE_TIME);
                if (eventDate.isAfter(LocalDateTime.now().minusDays(1))) {
                    ZonedDateTime zonedEventDate = ZonedDateTime.parse(event.getDate()).withZoneSameInstant(ZoneId.systemDefault());
                    event.setDate(zonedEventDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    events.add(event);
                }
            }
        }
        return events;
    }
}
