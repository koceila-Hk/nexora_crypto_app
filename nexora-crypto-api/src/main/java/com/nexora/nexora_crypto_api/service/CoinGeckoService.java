package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;

import java.util.Map;

public interface CoinGeckoService {
    Map<String, Object> getCryptoPrice(String id, String currency);
    CoinInfosForUserDto getCoinDetails(String coinId, String eur);
}
