package com.coinlift.backend.services.apis;

import com.coinlift.backend.entities.CryptoImage;
import com.coinlift.backend.pojo.CryptoEvent;
import com.coinlift.backend.repositories.CryptoImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class CryptoEventsService {

    private final CryptoImageRepository cryptoImageRepository;

    public CryptoEventsService(CryptoImageRepository cryptoImageRepository) {
        this.cryptoImageRepository = cryptoImageRepository;
    }

    private static final String API_BASE_URL = "https://api.coinpaprika.com/v1/coins/";

    private static final String API_EVENT_ENDPOINT = "/events";

    List<String> topCryptos = Arrays.asList("btc-bitcoin", "eth-ethereum",
            "bnb-binance-coin", "ada-cardano", "xrp-xrp");

    /**
     * Retrieves the latest crypto events for the top cryptocurrencies.
     *
     * @return A list of CryptoEvent objects representing the latest events.
     */
    public List<CryptoEvent> getLatestCryptoEvents() {
        RestTemplate restTemplate = new RestTemplate();

        return topCryptos.stream()
                .flatMap(crypto -> {
                    String apiUrl = API_BASE_URL + crypto + API_EVENT_ENDPOINT;
                    CryptoEvent[] cryptoEvents = restTemplate.getForObject(apiUrl, CryptoEvent[].class);
                    return (cryptoEvents != null ? Arrays.stream(cryptoEvents) : Arrays.stream(new CryptoEvent[0]))
                            .filter(event -> isEventWithinLast24Hours(event.getDate()))
                            .map(event -> createCryptoEvent(crypto, event));
                })
                .toList();
    }

    private boolean isEventWithinLast24Hours(String date) {
        LocalDateTime eventDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        return eventDate.isAfter(LocalDateTime.now().minusDays(1));
    }

    private CryptoEvent createCryptoEvent(String crypto, CryptoEvent event) {
        String result = crypto.substring(crypto.indexOf('-') + 1);

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

        return event;
    }
}
