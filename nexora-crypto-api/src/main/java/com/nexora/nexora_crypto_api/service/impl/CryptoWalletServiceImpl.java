package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.dto.WalletDetailDto;
import com.nexora.nexora_crypto_api.model.CryptoWallet;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.repository.CryptoWalletRepository;
import com.nexora.nexora_crypto_api.repository.TransactionRepository;
import com.nexora.nexora_crypto_api.service.CoinGeckoService;
import com.nexora.nexora_crypto_api.service.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CoinGeckoService coinGeckoService;

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
    public List<WalletDetailDto> getWalletsWithVariation(Long userId) {
        List<CryptoWallet> wallets = cryptoWalletRepository.findByUserId(userId);
        List<WalletDetailDto> result = new ArrayList<>();

        for (CryptoWallet wallet : wallets) {
            List<Object[]> buyData = transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(userId, wallet.getCryptoName());

            if (!buyData.isEmpty()) {
                Object[] row = buyData.get(0);
                BigDecimal totalBuyAmount = (BigDecimal) row[0];
                BigDecimal totalQuantity = (BigDecimal) row[1];

                if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal averageBuyPrice = totalBuyAmount.divide(totalQuantity, 4, RoundingMode.HALF_UP);
                    BigDecimal currentPrice = coinGeckoService.getCryptoPrice(wallet.getCryptoName().toLowerCase(), "eur");

                    BigDecimal variation = currentPrice.subtract(averageBuyPrice)
                            .divide(averageBuyPrice, 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

//                    wallet.setVariationPercentage(variation);

                    WalletDetailDto dto = new WalletDetailDto();
                    dto.setCryptoName(wallet.getCryptoName());
                    dto.setQuantity(wallet.getQuantity());
                    dto.setVariationPercentage(variation);

                    result.add(dto);
                }
            }
        }
        return result;
    }

}
