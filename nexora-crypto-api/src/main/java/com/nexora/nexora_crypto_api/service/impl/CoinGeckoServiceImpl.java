package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.dto.CoinDetailDto;
import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.service.CoinGeckoService;
import io.micrometer.observation.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CoinGeckoServiceImpl implements CoinGeckoService {

    @Value("${coingecko.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoServiceImpl.class);

    @Override
    public BigDecimal getCryptoPrice(String id, String currency) {
        try {
            String url = baseUrl + "simple/price?ids=" + id + "&vs_currencies=" + currency;

            ResponseEntity<Map<String, Map<String, BigDecimal>>> response =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {
                            });
            System.out.println("unitPrice : " + response.getBody().getOrDefault(id, Map.of()).getOrDefault(currency, BigDecimal.ZERO));
            return response.getBody().getOrDefault(id, Map.of()).getOrDefault(currency, BigDecimal.ZERO);
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RuntimeException("Many requests to CoinGecko !");
        }catch (HttpClientErrorException e) {
            throw new RuntimeException("Error : " + e.getStatusCode() + e.getMessage());
        }
    }

    @Override
    public CoinInfosForUserDto getCoinDetails(String coinId, String eur) {
        try{
            String url = baseUrl + "coins/" + coinId;
            ResponseEntity<CoinDetailDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            CoinDetailDto coinDetailDto = response.getBody();

            BigDecimal currentPrice = coinDetailDto.getMarket_data().getCurrent_price().getOrDefault(eur, BigDecimal.ZERO);
            BigDecimal percentageChange = coinDetailDto.getMarket_data().getPrice_change_percentage_24h();
            String icon = coinDetailDto.getImage().getSmall();

            return new CoinInfosForUserDto(coinDetailDto.getName(), coinDetailDto.getSymbol(), icon, currentPrice, percentageChange);

        } catch (HttpClientErrorException.TooManyRequests e) {

            logger.error("CoinGecko Rate Limit Exceeded: {}", e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite d’appel CoinGecko atteinte.");
        } catch (HttpClientErrorException e) {
            logger.error("Erreur HTTP depuis CoinGecko: {}, {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), "Erreur CoinGecko : " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Erreur interne dans getCoinDetails", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne", e);
        }

    }
}
