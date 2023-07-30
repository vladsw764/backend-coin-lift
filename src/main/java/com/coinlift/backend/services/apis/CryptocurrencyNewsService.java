package com.coinlift.backend.services.apis;

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
    private static final String API_ENDPOINT = "https://cryptocurrency-news2.p.rapidapi.com/v1/cointelegraph";
    private static final String HEADER_CONTENT_TYPE = "application/octet-stream";
    private static final String HEADER_API_KEY = "X-RapidAPI-Key";
    private static final String HEADER_API_HOST = "X-RapidAPI-Host";
    private static final int ARTICLE_LIMIT = 8;

    /**
     * Fetches the latest cryptocurrency news articles from the CoinTelegraph API.
     *
     * @return A list of `CryptocurrencyNews` objects, each representing a single cryptocurrency news article.
     */
    public List<CryptocurrencyNews> getNewsArticles() {
        List<CryptocurrencyNews> articles = new ArrayList<>();
        try {
            HttpResponse<String> response = getResponse();
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray articlesArray = jsonResponse.getJSONArray("data");
            int count = Math.min(articlesArray.length(), ARTICLE_LIMIT);
            for (int i = 0; i < count; i++) {
                JSONObject articleObject = articlesArray.getJSONObject(i);
                CryptocurrencyNews article = CryptocurrencyNews.builder()
                        .url(articleObject.getString("url"))
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
                .uri(URI.create(API_ENDPOINT))
                .header("content-type", HEADER_CONTENT_TYPE)
                .header(HEADER_API_KEY, apiKey)
                .header(HEADER_API_HOST, API_HOST)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}