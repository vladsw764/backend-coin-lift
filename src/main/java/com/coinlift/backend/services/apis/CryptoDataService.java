package com.coinlift.backend.services.apis;

import com.coinlift.backend.entities.CryptoImage;
import com.coinlift.backend.pojo.CryptoData;
import com.coinlift.backend.pojo.CryptoPercentData;
import com.coinlift.backend.repositories.CryptoImageRepository;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoDataService {
    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final CryptoImageRepository cryptoImageRepository;

    public CryptoDataService(CryptoImageRepository cryptoImageRepository) {
        this.cryptoImageRepository = cryptoImageRepository;
    }

    private final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

    public List<CryptoData> getAllCryptoData() {
        String allDataParameters = "limit=11";
        JSONObject json = new JSONObject(getApiResponse(url, allDataParameters));
        JSONArray data = json.getJSONArray("data");

        List<CryptoData> cryptoDataList = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            JSONObject cryptocurrency = data.getJSONObject(i);
            JSONObject quoteUSD = cryptocurrency.getJSONObject("quote").getJSONObject("USD");
            Integer id = cryptocurrency.getInt("id");

            CryptoData cryptoData = CryptoData.builder()
                    .name(cryptocurrency.getString("name"))
                    .imageUrl(getImageUrl(id))
                    .symbol(cryptocurrency.getString("symbol"))
                    .price(quoteUSD.getDouble("price"))
                    .percentChange1H(quoteUSD.getDouble("percent_change_1h"))
                    .percentChange1D(quoteUSD.getDouble("percent_change_24h"))
                    .percentChange1W(quoteUSD.getDouble("percent_change_7d"))
                    .marketCap(quoteUSD.getDouble("market_cap"))
                    .volumeUSD(quoteUSD.getLong("volume_24h"))
                    .circulatingSupply(cryptocurrency.getLong("circulating_supply"))
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
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI apiUrl = new URI(url + "?" + parameters);

            HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.setHeader("X-CMC_PRO_API_KEY", apiKey);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int responseCode = response.getCode();
                if (responseCode >= 300) {
                    throw new RuntimeException("Error connecting to API. Response Code: " + responseCode);
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        } catch (URISyntaxException | IOException | ParseException e) {
            throw new RuntimeException("Error connecting to API: " + e.getMessage());
        }

        throw new RuntimeException("API response is empty.");
    }

    private String getImageUrl(Integer id) {
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

            cryptoImage = new CryptoImage(id, imageUrl);
            cryptoImageRepository.save(cryptoImage);

            return imageUrl;
        }
    }
}