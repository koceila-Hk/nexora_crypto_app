package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.dto.CoinDetailDto;
import com.nexora.nexora_crypto_api.dto.CoinInfosForUserDto;
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
        System.out.println("unitPrice : " + response.getBody().getOrDefault(id, Map.of()).getOrDefault(currency, BigDecimal.ZERO));
        return response.getBody().getOrDefault(id, Map.of()).getOrDefault(currency, BigDecimal.ZERO);
    }


    public CoinInfosForUserDto getCoinDetails(String coinId, String eur) {
        String url = baseUrl + "coins/" + coinId;

        ResponseEntity<CoinDetailDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        CoinDetailDto coinDetailDto = response.getBody();

        BigDecimal currentPrice = coinDetailDto.getMarket_data().getCurrent_price().getOrDefault(eur, BigDecimal.ZERO);
        BigDecimal percentageChange = coinDetailDto.getMarket_data().getPrice_change_percentage_24h();
        String icon = coinDetailDto.getImage().getSmall();

        return new CoinInfosForUserDto(coinDetailDto.getName(), coinDetailDto.getSymbol(), icon, currentPrice, percentageChange);

    }
}
