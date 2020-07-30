package com.hackathon.crawler.processor;

import com.hackathon.crawler.data.entities.Transaction;
import com.hackathon.crawler.data.repository.BlockRepository;
import com.hackathon.crawler.processor.cache.CacheTxFilesHandler;
import com.hackathon.crawler.processor.cache.TransactionCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class Processor {

    private final BlockRepository blockRepository;
    private final DataReceiver dataReceiver;
    private final DataBuilder dataBuilder;
    private final Statistic statistic;
    private final TransactionCache transactionCache;
    private final CacheTxFilesHandler cacheTxFilesHandler;

    public Processor(BlockRepository blockRepository,
                     DataReceiver dataReceiver,
                     DataBuilder dataBuilder,
                     Statistic statistic,
                     TransactionCache transactionCache,
                     CacheTxFilesHandler cacheTxFilesHandler) {
        this.blockRepository = blockRepository;
        this.dataReceiver = dataReceiver;
        this.dataBuilder = dataBuilder;
        this.statistic = statistic;
        this.transactionCache = transactionCache;
        this.cacheTxFilesHandler = cacheTxFilesHandler;
    }

    public void doScript() {
        Long topHeightBlockInDb = checkNumberOfLastBlockInDB();
        Long topBlockHeight = dataReceiver.getTopBlocHeight();
        String topBlockHash = dataReceiver.getTopBlockHash();
        if (!topHeightBlockInDb.equals(topBlockHeight)) {
            System.out.println("--------------------------------------------------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------------------------------------------------");
            System.out.println("Start session!");
            long startSessionTime = System.currentTimeMillis();
            String nextBlockInJSON = dataReceiver.getBlockDataByNumber(topHeightBlockInDb + 1);
            for (long i = topHeightBlockInDb; i <= topBlockHeight; i++) {

                if (topBlockHeight.equals(i + 1)) {
                    dataBuilder.buildBlock(nextBlockInJSON, true, topBlockHash);
                    break;
                }
                nextBlockInJSON = dataBuilder.buildBlock(nextBlockInJSON, false, topBlockHash);
            }
            cacheTxFilesHandler.writeCacheInFile(transactionCache.getCacheInBytes());
            long finishSessionTime = System.currentTimeMillis();
            System.out.println("--------------------------------------------------------------------------------------------------------------");
            System.out.println(statistic.toString(startSessionTime, finishSessionTime));
            System.out.println("--------------------------------------------------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------------------------------------------------");
            statistic.refreshRequestCount();
        } else {
            System.out.println("There are no new blocks!");
        }
        System.out.println();
    }

    private Long checkNumberOfLastBlockInDB() {
        Long numberOfLastBlockInDB = blockRepository.getTopBlockNumber();
        return Objects.requireNonNullElse(numberOfLastBlockInDB, -1L);
    }

}
