package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.dto.CoinDetailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CoinGeckoService {

    @Value("${coingecko.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getCryptoPrice(String id, String currency) {
        String url = baseUrl + "simple/price?ids=" + id + "&vs_currencies=" + currency;

        ResponseEntity<Map<String, Map<String, BigDecimal>>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        });

        return response.getBody()
                .getOrDefault(id, Map.of())
                .getOrDefault(currency, BigDecimal.ZERO);
    }

    public CoinDetailDto getCoinDetails(String coinId) {
        String url = baseUrl + "coins/" + coinId;

        ResponseEntity<CoinDetailDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

}
