package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.CoinCache;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CoinCacheRepository extends MongoRepository<CoinCache, String> {
}
