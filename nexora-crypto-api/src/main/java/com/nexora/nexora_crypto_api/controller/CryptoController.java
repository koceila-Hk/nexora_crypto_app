package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.service.CoinGeckoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/crypto")
public class CryptoController {
    @Autowired
    private CoinGeckoService coinGeckoService;

    private static final Logger logger = LoggerFactory.getLogger(CryptoController.class);

    @GetMapping("/price")
    public ResponseEntity<BigDecimal> getPrice(@RequestParam String id, @RequestParam(defaultValue = "eur") String currency) {

        BigDecimal price = coinGeckoService.getCryptoPrice(id, currency);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CoinInfosForUserDto> getCoinDetails(@PathVariable String id, @RequestParam(defaultValue = "eur") String currency) {
        CoinInfosForUserDto details = coinGeckoService.getCoinDetails(id, currency);
        return ResponseEntity.ok(details);
    }

}
