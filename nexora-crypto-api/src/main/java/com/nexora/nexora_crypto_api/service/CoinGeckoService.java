package com.nexora.nexora_crypto_api.service;

import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;

import java.util.Map;

public interface CoinGeckoService {
    CoinInfosForUserDto getCoinDetails(String coinId, String eur);
}
