package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.service.CoinWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private CoinWalletService cryptoWalletService;

    @GetMapping("/variation/{userId}")
    public ResponseEntity<List<WalletDetailDto>> getWalletsWithVariation(@PathVariable Long userId) {
        List<WalletDetailDto> wallets = cryptoWalletService.getWalletsWithVariation(userId);

        if (wallets == null) {
            wallets = List.of();
        }

        return ResponseEntity.ok(wallets);
    }

}
