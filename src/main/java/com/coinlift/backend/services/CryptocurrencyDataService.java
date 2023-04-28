package com.coinlift.backend.services;

import com.coinlift.backend.pojo.CryptocurrencyData;
import org.json.JSONArray;
import org.json.JSONException;
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
public class CryptocurrencyDataService {
    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    public List<CryptocurrencyData> getCryptocurrencyData() throws IOException, JSONException, URISyntaxException {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        String parameters = "limit=10";

        URI apiUrl = new URI(url + "?" + parameters);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-CMC_PRO_API_KEY", apiKey);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray data = json.getJSONArray("data");

        List<CryptocurrencyData> cryptocurrencyDataList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject cryptocurrency = data.getJSONObject(i);
            String name = cryptocurrency.getString("name");
            double percentChange24h = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h");
            CryptocurrencyData cryptocurrencyData = new CryptocurrencyData(name, percentChange24h);
            cryptocurrencyDataList.add(cryptocurrencyData);
        }

        return cryptocurrencyDataList;
    }
}
