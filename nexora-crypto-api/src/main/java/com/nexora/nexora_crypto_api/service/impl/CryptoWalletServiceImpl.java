package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.dto.CoinInfosForUserDto;
import com.nexora.nexora_crypto_api.model.dto.WalletDetailDto;
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


    /**
     * Récupère un portefeuille d'un utilisateur pour une cryptomonnaie donnée,
     * ou en crée un s'il n'existe pas.
     *
     * @param cryptoName Nom de la cryptomonnaie
     * @param user       Utilisateur propriétaire du portefeuille
     * @return Le portefeuille existant ou nouvellement créé
     */
    @Override
    public CryptoWallet getOrCreateWallet(String cryptoName, User user) {
        return cryptoWalletRepository.findByUserIdAndCryptoName(user.getId(), cryptoName).orElseGet(() -> {
            CryptoWallet wallet = new CryptoWallet();
            wallet.setUser(user);
            wallet.setCryptoName(cryptoName);
            wallet.setQuantity(BigDecimal.ZERO);
            return cryptoWalletRepository.save(wallet);
        });
    }

    /**
     * Récupère tous les portefeuilles d'un utilisateur avec les variations
     * de prix par rapport au prix moyen d'achat.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des portefeuilles enrichis avec variations de prix et icônes
     */
    @Override
    public List<WalletDetailDto> getWalletsWithVariation(Long userId) {
        List<CryptoWallet> wallets = cryptoWalletRepository.findByUserId(userId);
        List<WalletDetailDto> result = new ArrayList<>();

        for (CryptoWallet wallet : wallets) {
            // Récupération des totaux d'achat
            List<Object[]> buyData = transactionRepository.findTotalAmountAndQuantityByUserAndCrypto(userId, wallet.getCryptoName());

            if (!buyData.isEmpty()) {
                Object[] row = buyData.get(0);
                BigDecimal totalBuyAmount = row[0] != null ? (BigDecimal) row[0] : BigDecimal.ZERO;
                BigDecimal totalQuantity = row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO;

                if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal averageBuyPrice = totalBuyAmount.divide(totalQuantity, 4, RoundingMode.HALF_UP);

                    // Utilisation de getCoinDetails pour récupérer prix et icône
                    CoinInfosForUserDto coinDetails = coinGeckoService.getCoinDetails(wallet.getCryptoName().toLowerCase(), "eur");
                    BigDecimal currentPrice = coinDetails.getCurrentPrice() != null ? coinDetails.getCurrentPrice() : BigDecimal.ZERO;

                    if (currentPrice.compareTo(BigDecimal.ZERO) > 0) {
                        // Calcul de la variation en pourcentage
                        BigDecimal variation = currentPrice.subtract(averageBuyPrice)
                                .divide(averageBuyPrice, 5, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));

                        WalletDetailDto dto = new WalletDetailDto();
                        dto.setCryptoName(wallet.getCryptoName());
                        dto.setQuantity(wallet.getQuantity());
                        dto.setVariationPercentage(variation);
                        dto.setIcon(coinDetails.getIcon());

                        result.add(dto);
                    }
                }
            }
        }

        return result;
    }
}
