package com.coinlift.backend.services;

import com.coinlift.backend.pojo.CryptoPercentData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoPercentDataService {
    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    public List<CryptoPercentData> getCryptoPercentData() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        String parameters = "limit=5";
        String response = getApiResponse(url, parameters);

        JSONObject json = new JSONObject(response);
        JSONArray data = json.getJSONArray("data");

        // Transform cryptocurrency data into CryptoPercentData objects
        List<CryptoPercentData> cryptoPercentDataList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject cryptocurrency = data.getJSONObject(i);
            String name = cryptocurrency.getString("name");
            double percentChange24h = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h");
            CryptoPercentData cryptoPercentData = new CryptoPercentData(name, Math.round(percentChange24h * 100.0) / 100.0);
            cryptoPercentDataList.add(cryptoPercentData);
        }

        return cryptoPercentDataList;
    }

    private String getApiResponse(String url, String parameters) {
        StringBuilder response = new StringBuilder();
        try {
            URI apiUrl = new URI(url + "?" + parameters);

            HttpURLConnection connection = (HttpURLConnection) apiUrl.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-CMC_PRO_API_KEY", apiKey);

            // Read API response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid API URL: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error connecting to API: " + e.getMessage());
        }

        return response.toString();
    }
}