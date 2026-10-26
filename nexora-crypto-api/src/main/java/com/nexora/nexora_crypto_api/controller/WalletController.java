package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private CryptoWalletService cryptoWalletService;

    @GetMapping("/variation/{userId}")
    public ResponseEntity<List<WalletDetailDto>> getWalletsWithVariation(@PathVariable Long userId, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<WalletDetailDto> wallets = cryptoWalletService.getWalletsWithVariation(userId);

        if (wallets == null) {
            wallets = List.of();
        }

        return ResponseEntity.ok(wallets);
    }

}
