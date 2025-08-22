package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.CryptoCache;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CryptoCacheRepository extends MongoRepository<CryptoCache, String> {
}
