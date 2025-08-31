package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CoinCache;
import com.nexora.nexora_crypto_api.model.dto.CoinDetailDto;
import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.repository.CryptoCacheRepository;
import com.nexora.nexora_crypto_api.service.CoinGeckoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;
import java.util.Map;

@Service
public class CoinGeckoServiceImpl implements CoinGeckoService {

    @Autowired
    private CryptoCacheRepository cryptoCacheRepository;

    @Value("${coingecko.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoServiceImpl.class);


    @Override
    public Map<String, Object> getCryptoPrice(String id, String currency) {
        try {
            // Récupérer le prix
            String priceUrl = baseUrl + "simple/price?ids=" + id + "&vs_currencies=" + currency;
            ResponseEntity<Map<String, Map<String, BigDecimal>>> priceResponse =
                    restTemplate.exchange(priceUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {});
            BigDecimal price = priceResponse.getBody()
                    .getOrDefault(id, Map.of())
                    .getOrDefault(currency, BigDecimal.ZERO);

            // Récupérer l’icône
            String infoUrl = baseUrl + "coins/" + id;
            ResponseEntity<Map<String, Object>> infoResponse =
                    restTemplate.exchange(infoUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {});
            @SuppressWarnings("unchecked")
            Map<String, String> imageMap = (Map<String, String>) infoResponse.getBody().get("image");
            String icon = imageMap != null ? imageMap.get("small") : null;

            // Retourner directement une map
            return Map.of(
                    "price", price,
                    "icon", icon
            );

        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RuntimeException("Many requests to CoinGecko !");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error : " + e.getStatusCode() + " " + e.getMessage());
        }
    }

    @Override
    public CoinInfosForUserDto getCoinDetails(String coinId, String eur) {
        try {
            // Vérifier si le coin existe déjà en cache
            return cryptoCacheRepository.findById(coinId)
                    .filter(cached -> cached.getCachedAt() != null)
                    .map(cached -> new CoinInfosForUserDto(
                            cached.getName(),
                            cached.getSymbol(),
                            cached.getIcon(),
                            cached.getPrice(),
                            cached.getPercentageChange()
                    ))
                    .orElseGet(() -> fetchAndCacheCoinGecko(coinId, eur));
        } catch (Exception e) {
            logger.error("Erreur interne dans getCoinDetails", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne", e);
        }
    }


    private CoinInfosForUserDto fetchAndCacheCoinGecko(String coinId, String eur) {
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

            // Sauvegarder en cache Mongo
            CoinCache cryptoCache = new CoinCache();
            cryptoCache.setId(coinId);
            cryptoCache.setName(coinDetailDto.getName());
            cryptoCache.setSymbol(coinDetailDto.getSymbol());
            cryptoCache.setIcon(icon);
            cryptoCache.setPrice(currentPrice);
            cryptoCache.setPercentageChange(percentageChange);
            cryptoCache.setCachedAt(new Date());
            cryptoCacheRepository.save(cryptoCache);

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
