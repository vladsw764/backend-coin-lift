package com.coinlift.backend.controllers;

import com.coinlift.backend.pojo.CryptoData;
import com.coinlift.backend.pojo.CryptoEvent;
import com.coinlift.backend.pojo.CryptoPercentData;
import com.coinlift.backend.pojo.CryptocurrencyNews;
import com.coinlift.backend.services.apis.CryptoDataService;
import com.coinlift.backend.services.apis.CryptoEventsService;
import com.coinlift.backend.services.apis.CryptocurrencyNewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CryptocurrencyControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private CryptoDataService cryptoDataService;
    @MockBean
    private CryptocurrencyNewsService cryptocurrencyNewsService;
    @MockBean
    private CryptoEventsService cryptoEventsService;

    private List<CryptoPercentData> cryptoPercentData;
    private List<CryptoData> cryptoData;
    private List<CryptocurrencyNews> cryptocurrencyNews;
    private List<CryptoEvent> cryptoEvents;

    @BeforeEach
    void setUp() {
        cryptoPercentData = List.of(
                new CryptoPercentData("Bitcoin", 10.5, "https://example.com/bitcoin.png"),
                new CryptoPercentData("Ethereum", 7.2, "https://example.com/ethereum.png"),
                new CryptoPercentData("Litecoin", -2.8, "https://example.com/litecoin.png")
        );
        cryptoData = List.of(
                new CryptoData("https://example.com/bitcoin.png", "Bitcoin", "BTC", 50000.0, 1.2,
                        2.3, -4.5, 100000000000.0, 50000000000L, 25000000000L),
                new CryptoData("https://example.com/ethereum.png", "Ethereum", "ETH", 2500.0, -0.5,
                        3.2, 7.8, 30000000000.0, 10000000000L, 8000000000L)
        );

        cryptocurrencyNews = List.of(
                new CryptocurrencyNews("Bitcoin News", "https://example.com/bitcoin-news", "Latest news about Bitcoin",
                        "https://example.com/bitcoin-thumbnail", "2023-07-01T09:30:00Z"),
                new CryptocurrencyNews("Ethereum News", "https://example.com/ethereum-news", "Latest news about Ethereum",
                        "https://example.com/ethereum-thumbnail", "2023-07-02T12:15:00Z")
        );

        cryptoEvents = List.of(
                new CryptoEvent("Bitcoin", "2023-07-01", "https://example.com/bitcoin-event-image",
                        "https://example.com/bitcoin-event-link", true),
                new CryptoEvent("Ethereum", "2023-07-02", "https://example.com/ethereum-event-image",
                        "https://example.com/ethereum-event-link", false)
        );

    }

    @Test
    void getCryptoPercentData() throws Exception {

        when(cryptoDataService.getCryptoPercentData()).thenReturn(cryptoPercentData);

        mockMvc.perform(get("/api/v1/cryptocurrency/percent-data"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(cryptoPercentData.size()))
                .andExpect(jsonPath("$[0].name").value(cryptoPercentData.get(0).getName()))
                .andExpect(jsonPath("$[0].percentChange24h").value(cryptoPercentData.get(0).getPercentChange24h()))
                .andExpect(jsonPath("$[0].imageUrl").value(cryptoPercentData.get(0).getImageUrl()))
                .andDo(print());
    }

    @Test
    void getCryptoData() throws Exception {
        when(cryptoDataService.getAllCryptoData()).thenReturn(cryptoData);

        mockMvc.perform(get("/api/v1/cryptocurrency/all-data"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(cryptoData.size()))
                .andExpect(jsonPath("$[0].imageUrl").value(cryptoData.get(0).getImageUrl()))
                .andExpect(jsonPath("$[0].name").value(cryptoData.get(0).getName()))
                .andExpect(jsonPath("$[0].symbol").value(cryptoData.get(0).getSymbol()))
                .andExpect(jsonPath("$[0].price").value(cryptoData.get(0).getPrice()))
                .andExpect(jsonPath("$[0].percentChange1H").value(cryptoData.get(0).getPercentChange1H()))
                .andExpect(jsonPath("$[0].percentChange1D").value(cryptoData.get(0).getPercentChange1D()))
                .andExpect(jsonPath("$[0].percentChange1W").value(cryptoData.get(0).getPercentChange1W()))
                .andExpect(jsonPath("$[0].marketCap").value(cryptoData.get(0).getMarketCap()))
                .andExpect(jsonPath("$[0].volumeUSD").value(cryptoData.get(0).getVolumeUSD()))
                .andExpect(jsonPath("$[0].circulatingSupply").value(cryptoData.get(0).getCirculatingSupply()))
                .andDo(print());
    }

    @Test
    void getCryptocurrencyNews() throws Exception {
        when(cryptocurrencyNewsService.getNewsArticles()).thenReturn(cryptocurrencyNews);

        mockMvc.perform(get("/api/v1/cryptocurrency/news"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(cryptocurrencyNews.size()))
                .andExpect(jsonPath("$[0].title").value(cryptocurrencyNews.get(0).getTitle()))
                .andExpect(jsonPath("$[0].url").value(cryptocurrencyNews.get(0).getUrl()))
                .andExpect(jsonPath("$[0].description").value(cryptocurrencyNews.get(0).getDescription()))
                .andExpect(jsonPath("$[0].thumbnail").value(cryptocurrencyNews.get(0).getThumbnail()))
                .andExpect(jsonPath("$[0].createdAt").value(cryptocurrencyNews.get(0).getCreatedAt()))
                .andDo(print());
    }

    @Test
    void getCryptoEvent() throws Exception {
        when(cryptoEventsService.getLatestCryptoEvents()).thenReturn(cryptoEvents);

        mockMvc.perform(get("/api/v1/cryptocurrency/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(cryptoEvents.size()))
                .andExpect(jsonPath("$[0].cryptoName").value(cryptoEvents.get(0).getCryptoName()))
                .andExpect(jsonPath("$[0].date").value(cryptoEvents.get(0).getDate()))
                .andExpect(jsonPath("$[0].imageLink").value(cryptoEvents.get(0).getImageLink()))
                .andExpect(jsonPath("$[0].link").value(cryptoEvents.get(0).getLink()))
                .andExpect(jsonPath("$[0].is_conference").value(cryptoEvents.get(0).isConference()))
                .andDo(print());
    }
}