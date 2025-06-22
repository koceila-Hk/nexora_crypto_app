package com.nexora.nexora_crypto_api.mapper;

import com.nexora.nexora_crypto_api.dto.TransactionDto;
import com.nexora.nexora_crypto_api.model.Transaction;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TransactionMapper {
    public static TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setCryptoName(transaction.getCryptoName());
        dto.setType(transaction.getType());
        dto.setQuantity(transaction.getQuantity());
        dto.setUnitPrice(transaction.getUnitPrice());
        dto.setTotalAmount(transaction.getTotalAmount());
        dto.setDateTransaction(transaction.getDateTransaction());
        return dto;
    }

    public static List<TransactionDto> dtoList(List<Transaction> transactions) {
        return transactions.stream().map(TransactionMapper::toDto).collect(Collectors.toList());
    }
            // with boucle for
//        List<TransactionDto> result = new ArrayList<>();
//            for (Transaction t : transactions) {
//            result.add(TransactionMapper.toDto(t));
//        }
//            return result;
//
}
