package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private CryptoWalletService cryptoWalletService;

    @GetMapping("/variation/{userId}")
    public List<WalletDetailDto> getWalletsWithVariation(@PathVariable Long userId) {
        return cryptoWalletService.getWalletsWithVariation(userId);
    }
}
