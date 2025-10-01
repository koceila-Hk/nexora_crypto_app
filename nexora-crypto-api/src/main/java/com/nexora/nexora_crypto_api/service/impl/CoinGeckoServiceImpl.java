package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoCache;
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

// Service pour interagir avec l'API CoinGecko.
@Service
public class CoinGeckoServiceImpl implements CoinGeckoService {

    @Autowired
    private CryptoCacheRepository cryptoCacheRepository;

    @Value("${coingecko.api.url}")
    String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoServiceImpl.class);


    /**
     * Récupère les détails d'une cryptomonnaie à partir du cache mongodb ou de l'API CoinGecko.
     * Si la donnée n'est pas en cache "mongodb", elle est récupérée depuis CoinGecko puis stockée dans mongodb d'une durée de 4min.
     *
     * @param coinId L’identifiant de la cryptomonnaie "bitcoin"
     * @param eur    La devise (toujours "eur" dans ce contexte)
     * @return Un objet contenant nom, symbole, prix, icône et variation de la crypto
     * @throws ResponseStatusException en cas d'erreur interne ou d'erreur HTTP
     */
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

    /**
     * Récupère les informations détaillées d'une cryptomonnaie depuis l'API CoinGecko,
     * puis les stocke en cache mongoDb" pour de futures requêtes.
     *
     * @param coinId L'identifiant de la cryptomonnaie
     * @param eur    La devise "eur"
     * @return Objet DTO contenant les détails nécessaires à l'affichage pour l'utilisateur
     */
    public CoinInfosForUserDto fetchAndCacheCoinGecko(String coinId, String eur) {
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

            if (coinDetailDto == null || coinDetailDto.getMarket_data() == null) {
                logger.error("Response CoinGecko invali for coinId = {}", coinId);
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "response coinGecko invalid");
            }

            BigDecimal currentPrice = coinDetailDto.getMarket_data().getCurrent_price().getOrDefault(eur, BigDecimal.ZERO);
            BigDecimal percentageChange = coinDetailDto.getMarket_data().getPrice_change_percentage_24h();
            String icon = coinDetailDto.getImage().getSmall();

            // Sauvegarder en cache Mongo
            CryptoCache cryptoCache = new CryptoCache();
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
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many request to CoinGecko.");
        } catch (HttpClientErrorException e) {
            logger.error("Error HTTP CoinGecko: {}, {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), "Error CoinGecko : " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error interne getCoinDetails", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interne", e);
        }

    }
}
