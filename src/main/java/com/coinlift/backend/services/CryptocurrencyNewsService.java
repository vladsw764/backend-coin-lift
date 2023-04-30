package com.coinlift.backend.services;

import com.coinlift.backend.pojo.CryptocurrencyNews;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CryptocurrencyNewsService {
    @Value("${rapid.coindesk.api.key}")
    private String apiKey;
    private static final String API_HOST = "cryptocurrency-news2.p.rapidapi.com";

    public List<CryptocurrencyNews> getCoinDeskArticles() {
        List<CryptocurrencyNews> articles = new ArrayList<>();
        try {
            HttpResponse<String> response = getResponse();
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray articlesArray = jsonResponse.getJSONArray("data");
            int count = Math.min(articlesArray.length(), 10);
            for (int i = 0; i < count; i++) {
                JSONObject articleObject = articlesArray.getJSONObject(i);
                CryptocurrencyNews article = CryptocurrencyNews.builder()
                        .title(articleObject.getString("title"))
                        .description(articleObject.getString("description"))
                        .thumbnail(articleObject.getString("thumbnail"))
                        .createdAt(articleObject.getString("createdAt"))
                        .build();
                articles.add(article);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }

    private HttpResponse<String> getResponse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://cryptocurrency-news2.p.rapidapi.com/v1/coindesk"))
                .header("content-type", "application/octet-stream")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", API_HOST)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}