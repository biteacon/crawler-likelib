package com.hackathon.crawler.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hackathon.crawler.data.entities.Account;
import com.hackathon.crawler.data.entities.Block;
import com.hackathon.crawler.data.entities.Transaction;
import com.hackathon.crawler.data.repository.AccountRepository;
import com.hackathon.crawler.data.repository.BlockRepository;
import com.hackathon.crawler.data.repository.TransactionRepository;
import com.hackathon.crawler.processor.receivers.IDataReceiver;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Component
public class BlockBuilder {

    private final IDataReceiver dataReceiver;
    private final BlockRepository blockRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BlockBuilder(IDataReceiver dataReceiver,
                        BlockRepository blockRepository,
                        AccountRepository accountRepository,
                        TransactionRepository transactionRepository) {
        this.dataReceiver = dataReceiver;
        this.blockRepository = blockRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Block buildBlock(String hash) {

        Block block = blockFromJSON(dataReceiver.getBlockDataByHash(hash));
        block.setHash(hash);

        Account coinbaseAccount = accountFromJSON(dataReceiver.getAccountData(block.getCoinbaseString()));

        block.setCoinbase(accountRepository.save(coinbaseAccount));//

        Set<Transaction> transactionSet = getTransactionSetFromJSON(block.getTransactionsInJSON());

        for (Transaction transaction : transactionSet) {
            Account accountTo = accountFromJSON(dataReceiver.getAccountData(transaction.getAccountToString()));
            Account accountFrom = accountFromJSON(dataReceiver.getAccountData(transaction.getAccountFromString()));

            transaction.setAccountTo(accountRepository.save(accountTo));
            transaction.setAccountFrom(accountRepository.save(accountFrom));

            for (String currentTxHash : accountTo.getTransactionsHashesInString()) {
                Transaction currentTransaction = getTransactionFromJSON(dataReceiver.getTransactionData(currentTxHash));

                if (transactionSet.contains(currentTransaction)) {
                    String transactionStatusJSON = dataReceiver.getTransactionStatusData(currentTxHash);
                    writeExtraValuesToTransaction(transaction, currentTxHash, transactionStatusJSON);
                }
            }

            for (String currentTxHash : accountFrom.getTransactionsHashesInString()) {
                Transaction currentTransaction = getTransactionFromJSON(dataReceiver.getTransactionData(currentTxHash));

                if (transactionSet.contains(currentTransaction)) {
                    String transactionStatusJSON = dataReceiver.getTransactionStatusData(currentTxHash);
                    writeExtraValuesToTransaction(transaction, currentTxHash, transactionStatusJSON);
                }
            }
        }

        Block b = blockRepository.save(block);

        for (Transaction tx : transactionSet) {
            tx.setBlock(b);
        }

        transactionRepository.saveAll(transactionSet);

        //block.setTransactionList(transactionSet);


        return block;
    }

    private Block blockFromJSON(String json) {
        Block block = new Block();
        try {
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

            block.setHeight(node.get("depth").asLong());
            block.setNonce(node.get("nonce").bigIntegerValue());
            block.setPrevBlockHash(node.get("previous_block_hash").asText());
            block.setTimestamp(LocalDateTime.ofInstant
                    (Instant.ofEpochMilli(node.get("timestamp").asLong() * 1000), ZoneId.systemDefault()));
            block.setCoinbaseString(node.get("coinbase").asText());
            block.setTransactionsInJSON(node.get("transactions").toString());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("");
        }

        return block;
    }

    private Account accountFromJSON(String json) {
        Account account = new Account();

        try {
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

            account.setAddress(node.get("address").asText());
            account.setBalance(Long.parseLong(node.get("balance").asText()));
            account.setNonce(node.get("nonce").bigIntegerValue());
            account.setType(node.get("type").asText());

            JsonNode jsonArray = new ObjectMapper().readTree(json).get("transaction_hashes");
            Set<String> transactionHashSet = new HashSet<>();
            for (JsonNode jsonNode : jsonArray) {
                transactionHashSet.add(jsonNode.asText());
            }
            account.setTransactionsHashesInString(transactionHashSet);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return account;
    }

    private Set<Transaction> getTransactionSetFromJSON(String json) {
        Set<Transaction> transactionSet = new HashSet<>();

        try {
            ObjectNode[] arrayNode = new ObjectMapper().readValue(json, ObjectNode[].class);
            for (ObjectNode jsonNodes : arrayNode) {
                Transaction transaction = getTransactionFromJSON(jsonNodes.toString());
                transactionSet.add(transaction);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return transactionSet;
    }

    private Transaction getTransactionFromJSON(String json) {
        Transaction transaction = new Transaction();

        try {
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

            transaction.setTimestamp(LocalDateTime.ofInstant
                    (Instant.ofEpochMilli(node.get("timestamp").asLong() * 1000), ZoneId.systemDefault()));
            transaction.setAccountToString(node.get("to").asText());
            transaction.setAccountFromString(node.get("from").asText());
            transaction.setData(node.get("data").asText());
            transaction.setAmount(node.get("amount").asLong());
            transaction.setSign(node.get("sign").asText());
            transaction.setFee(node.get("fee").asLong());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return transaction;
    }

    private void writeExtraValuesToTransaction(Transaction transaction, String hash, String json) {
        transaction.setHash(hash);

        try {
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
            byte[] messageInBytes = Base64.getDecoder().decode(node.get("message").asText());
            transaction.setMessage(new String(messageInBytes));
            transaction.setType(chooseTransactionType(node.get("action_type").asInt()));
            transaction.setStatus(chooseTransactionStatus(node.get("status_code").asInt()));
            transaction.setFeeLeft(node.get("fee_left").asLong());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String chooseTransactionType(int key) {
        switch (key) {
            case 0: return "None";
            case 1: return "Transfer";
            case 2: return "ContractCall";
            case 3: return "ContractCreation";
            default: return "";
        }
    }

    private String chooseTransactionStatus(int key) {
        switch (key) {
            case 0: return "Success";
            case 1: return "Pending";
            case 2: return "BadQueryForm";
            case 3: return "BadSign";
            case 4: return "NotEnoughBalance";
            case 5: return "Revert";
            case 6: return "Failed";
            default: return "";
        }
    }
}
