package com.coinlift.cryptoservice.service;

import com.coinlift.cryptoservice.model.CryptoImage;
import com.coinlift.cryptoservice.pojo.CryptoData;
import com.coinlift.cryptoservice.pojo.CryptoPercentData;
import com.coinlift.cryptoservice.repository.CryptoImageRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service@RequiredArgsConstructor
public class CryptoDataService {
    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final CryptoImageRepository cryptoImageRepository;
    private final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

    public List<CryptoData> getAllCryptoData() {
        String allDataParameters = "limit=11";
        JSONObject json = new JSONObject(getApiResponse(url, allDataParameters));
        JSONArray data = json.getJSONArray("data");

        List<CryptoData> cryptoDataList = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            JSONObject cryptocurrency = data.getJSONObject(i);

            String name = cryptocurrency.getString("name");
            String symbol = cryptocurrency.getString("symbol");
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
                    .symbol(symbol)
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
        // Check if the image URL exists in the database
        CryptoImage cryptoImage = cryptoImageRepository.findByCryptoId(id);
        if (cryptoImage != null) {
            return cryptoImage.getCryptoImageUrl();
        } else {
            String infoUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
            String infoParams = "id=" + id;
            String infoResponse = getApiResponse(infoUrl, infoParams);
            JSONObject infoJson = new JSONObject(infoResponse);
            JSONObject coinInfo = infoJson.getJSONObject("data").getJSONObject(String.valueOf(id));
            String imageUrl = coinInfo.getString("logo");

            // Save the image URL to the database
            cryptoImage = new CryptoImage();
            cryptoImage.setCryptoId(id);
            cryptoImage.setCryptoName(getCryptoNameById(id));
            cryptoImage.setCryptoImageUrl(imageUrl);
            cryptoImageRepository.save(cryptoImage);

            return imageUrl;
        }
    }

    private String getCryptoNameById(Integer id) {
        // Map cryptoId to cryptoName
        Map<Integer, String> cryptoNames = new HashMap<>();
        cryptoNames.put(1, "btc-bitcoin");
        cryptoNames.put(1027, "eth-ethereum");
        cryptoNames.put(1839, "bnb-binance-coin");
        cryptoNames.put(2010, "ada-cardano");
        cryptoNames.put(52, "xrp-xrp");

        return cryptoNames.getOrDefault(id, "");
    }
}