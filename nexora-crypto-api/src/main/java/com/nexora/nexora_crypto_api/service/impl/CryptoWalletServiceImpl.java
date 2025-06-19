package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;

    @Override
    public CryptoWallet getOrCreateWallet(String cryptoName, User user) {
        return cryptoWalletRepository.findByUserAndCryptoName(user, cryptoName).orElseGet(() -> {
            CryptoWallet wallet = new CryptoWallet();
            wallet.setUser(user);
            wallet.setCryptoName(cryptoName);
            wallet.setQuantity(BigDecimal.ZERO);
            return cryptoWalletRepository.save(wallet);
        });
    }

    @Override
    public List<CryptoWallet> getWalletsWithVariation(Long userId) {
        List<CryptoWallet> wallets = walletRepository.findByUserId(userId);

        for (CryptoWallet wallet : wallets) {
            Object[] buyData = transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(userId, wallet.getCryptoName());

            if (buyData != null && buyData[0] != null && buyData[1] != null) {
                BigDecimal totalBuyAmount = (BigDecimal) buyData[0];
                BigDecimal totalQuantity = (BigDecimal) buyData[1];

                if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal averageBuyPrice = totalBuyAmount.divide(totalQuantity, 4, RoundingMode.HALF_UP);
                    BigDecimal currentPrice = coinGeckoService.getCryptoPrice(wallet.getCryptoName(), "eur");

                    BigDecimal variation = currentPrice.subtract(averageBuyPrice)
                            .divide(averageBuyPrice, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

                    wallet.setVariationPercentage(variation);
                }
            }
        }

        return wallets;
    }

}
