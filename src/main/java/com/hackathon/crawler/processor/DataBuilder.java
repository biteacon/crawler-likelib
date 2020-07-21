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
import com.hackathon.crawler.processor.exceptions.ErrorCode;
import com.hackathon.crawler.processor.exceptions.ProcessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
public class DataBuilder {

    private final DataReceiver dataReceiver;
    private final BlockRepository blockRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionCache transactionCache;
    private final Statistic statistic;

    public DataBuilder(DataReceiver dataReceiver,
                       BlockRepository blockRepository,
                       AccountRepository accountRepository,
                       TransactionRepository transactionRepository,
                       TransactionCache transactionCache,
                       Statistic statistic) {
        this.dataReceiver = dataReceiver;
        this.blockRepository = blockRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionCache = transactionCache;
        this.statistic = statistic;
    }

    @Transactional
    public String buildBlock(String json, boolean isTopHeight, String hashIsTopHeight) {

        long startBuildingData = System.currentTimeMillis();
        Block block = blockFromJSON(json);
        String nextBlockJSON = "";
        if (isTopHeight) {
            block.setHash(hashIsTopHeight);
        } else {
            nextBlockJSON = dataReceiver.getBlockDataByNumber(block.getHeight() + 1);
            Block nextBlock = blockFromJSON(nextBlockJSON);
            block.setHash(nextBlock.getPrevBlockHash());
        }

        Account coinbaseAccount = accountFromJSON(dataReceiver.getAccountData(block.getCoinbaseString()));

        block.setCoinbase(saveAccountIfNotExist(coinbaseAccount));

        Set<Transaction> transactionSet = getTransactionSetFromJSON(block.getTransactionsInJSON());

        for (Transaction blockTransaction : transactionSet) {
            Account accountTo = accountFromJSON(dataReceiver.getAccountData(blockTransaction.getAccountToString()));
            Account accountFrom = accountFromJSON(dataReceiver.getAccountData(blockTransaction.getAccountFromString()));

            blockTransaction.setAccountTo(updateAccountIfExist(accountTo));
            blockTransaction.setAccountFrom(updateAccountIfExist(accountFrom));

            findTransactionsOfBlock(transactionSet, blockTransaction, accountTo.getTransactionsHashesInString());
            findTransactionsOfBlock(transactionSet, blockTransaction, accountFrom.getTransactionsHashesInString());
        }

        Block resultBlock = blockRepository.save(block);
        statistic.setBlockCountInc();

        for (Transaction tx : transactionSet) {
            if (tx.getHash() == null) {
                tx.setHash("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB=");
                tx.setStatus("Success");
                tx.setFeeLeft(0L);
                tx.setType("Transfer");
                tx.setMessage("");
            }
            tx.setBlock(resultBlock);
            statistic.setTxCountInc();
        }

        transactionRepository.saveAll(transactionSet);

        block.setTransactionList(transactionSet);

        long finishBuildingData = System.currentTimeMillis();
        float durationBuilding = (float) (finishBuildingData - startBuildingData)/1000;
        String formattedDurationTime = String.format("%.2f", durationBuilding);
        System.out.println("Received block with hash: " + block.getHash()
                + " and height: " + block.getHeight() + " | Duration: " + formattedDurationTime + " sec");

        if (isTopHeight)
            return json;
        else return nextBlockJSON;
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
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t parse JSON to block");
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
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t parse JSON to account.");
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
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t parse JSON to set of transactions.");
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
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t parse JSON to transactions.");
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
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t parse JSON to status of transactions.");
        }
    }

    private Account saveAccountIfNotExist(Account account) {

        if (accountRepository.existsByAddress(account.getAddress())) {
            return account;
        } else {
            return accountRepository.save(account);
        }
    }

    private Account updateAccountIfExist(Account account) {
        if (accountRepository.existsByAddress(account.getAddress())) {

            Account addressFromDb = accountRepository.findById(account.getAddress()).orElseThrow(
                    () -> new ProcessException(ErrorCode.PROCESS_EXCEPTION, "Can`t find address"));

            addressFromDb.setBalance(account.getBalance());
            addressFromDb.setNonce(account.getNonce());

            return accountRepository.save(addressFromDb);
        } else {
            return accountRepository.save(account);
        }
    }

    private void findTransactionsOfBlock(Set<Transaction> transactionSet,
                                                Transaction blockTransaction,
                                                Set<String> transactionHashes) {
        for (String currentTxHash : transactionHashes) {
            Transaction currentTransaction;
            if (transactionCache.isContainTransactionByHash(currentTxHash)) {
                currentTransaction = transactionCache.getTransactionByHash(currentTxHash);
            } else {
                currentTransaction = getTransactionFromJSON(dataReceiver.getTransactionData(currentTxHash));
                String transactionStatusJSON = dataReceiver.getTransactionStatusData(currentTxHash);
                writeExtraValuesToTransaction(currentTransaction, currentTxHash, transactionStatusJSON);
                transactionCache.putTransaction(currentTransaction);
                statistic.setTxInCacheInc();
            }

            if (transactionSet.contains(currentTransaction)) {
                String transactionStatusJSON = dataReceiver.getTransactionStatusData(currentTxHash);
                writeExtraValuesToTransaction(blockTransaction, currentTxHash, transactionStatusJSON);
            }
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