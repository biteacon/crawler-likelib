package com.hackathon.crawler.processor;

import com.hackathon.crawler.data.entities.Transaction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class TransactionCache {

    private final Map<String, Transaction> mapTransactionOfAccount = new HashMap<>();

    public boolean isContainTransactionByHash(String hash) {
        return mapTransactionOfAccount.containsKey(hash);
    }

    public void putTransaction(Transaction transaction) {
        mapTransactionOfAccount.put(transaction.getHash(), transaction);
    }

    public Transaction getTransactionByHash(String hash) {
        return mapTransactionOfAccount.get(hash);
    }

    public void clearCache() {
        mapTransactionOfAccount.clear();
    }

}
