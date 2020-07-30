package com.hackathon.crawler.processor.cache;

import com.hackathon.crawler.data.entities.Transaction;
import com.hackathon.crawler.processor.exceptions.ErrorCode;
import com.hackathon.crawler.processor.exceptions.ProcessException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public void fillCacheFromBytes(byte[] txCache) {
        if (txCache != null && txCache.length != 0) {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(txCache);
            try (ObjectInputStream in = new ObjectInputStream(byteIn)) {
                Map<String, Transaction> mapFromFile = (Map<String, Transaction>) in.readObject();
                if (!mapFromFile.isEmpty())
                    mapTransactionOfAccount.putAll(mapFromFile);
            } catch (IOException | ClassNotFoundException e) {
                throw new ProcessException(ErrorCode.PROCESS_EXCEPTION, "Can`t parse bytes of transactions. " + e.getMessage());
            }
        }
    }

    public byte[] getCacheInBytes() {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(mapTransactionOfAccount);
        } catch (IOException e) {
            throw new ProcessException(ErrorCode.PROCESS_EXCEPTION, "Can`t parse transactions to bytes.");
        }
        return byteOut.toByteArray();
    }
}
