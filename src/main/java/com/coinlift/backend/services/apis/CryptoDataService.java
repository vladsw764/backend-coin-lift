package com.coinlift.backend.services.apis;

import com.coinlift.backend.pojo.CryptoData;
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
public class CryptoDataService {
    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

    public List<CryptoData> getAllCryptoData() {
        String allDataParameters = "limit=11";
        JSONObject json = new JSONObject(getApiResponse(url, allDataParameters));
        JSONArray data = json.getJSONArray("data");

        List<CryptoData> cryptoDataList = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            JSONObject cryptocurrency = data.getJSONObject(i);

            String name = cryptocurrency.getString("name");
            Integer id = cryptocurrency.getInt("id");
            double price = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("price");
            double percentChange1H = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_1h");
            double percentChange1D = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h");
            double percentChange1W = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_7d");
            double marketCap = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("market_cap");
            long volumeUSD = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getLong("volume_24h");
            long circulatingSupply = cryptocurrency.getLong("circulating_supply");

            String imgUrl = getImageUrl(id);

            CryptoData cryptoData = CryptoData.builder()
                    .name(name)
                    .imageUrl(imgUrl)
                    .price(price)
                    .percentChange1H(percentChange1H)
                    .percentChange1D(percentChange1D)
                    .percentChange1W(percentChange1W)
                    .marketCap(marketCap)
                    .volumeUSD(volumeUSD)
                    .circulatingSupply(circulatingSupply)
                    .build();

            cryptoDataList.add(cryptoData);
        }

        return cryptoDataList;

    }

    public List<CryptoPercentData> getCryptoPercentData() {
        String percentDataParameters = "limit=5";
        String response = getApiResponse(url, percentDataParameters);
        JSONObject json = new JSONObject(response);
        JSONArray data = json.getJSONArray("data");

        // Transform cryptocurrency data into CryptoPercentData objects
        List<CryptoPercentData> cryptoPercentDataList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject cryptocurrency = data.getJSONObject(i);
            String name = cryptocurrency.getString("name");
            Integer id = cryptocurrency.getInt("id");
            double percentChange24h = cryptocurrency.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h");

            String imgUrl = getImageUrl(id);

            CryptoPercentData cryptoPercentData =
                    new CryptoPercentData(name, Math.round(percentChange24h * 100.0) / 100.0, imgUrl);
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

    private String getImageUrl(Integer id) {
        String infoUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
        String infoParams = "id=" + id;
        String infoResponse = getApiResponse(infoUrl, infoParams);
        JSONObject infoJson = new JSONObject(infoResponse);
        JSONObject coinInfo = infoJson.getJSONObject("data").getJSONObject(String.valueOf(id));
        return coinInfo.getString("logo");
    }
}