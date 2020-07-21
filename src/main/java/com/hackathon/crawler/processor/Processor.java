package com.hackathon.crawler.processor;

import com.hackathon.crawler.data.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class Processor {

    private final BlockRepository blockRepository;
    private final DataReceiver dataReceiver;
    private final DataBuilder dataBuilder;
    private final Statistic statistic;

    public Processor(BlockRepository blockRepository,
                     DataReceiver dataReceiver,
                     DataBuilder dataBuilder,
                     Statistic statistic) {
        this.blockRepository = blockRepository;
        this.dataReceiver = dataReceiver;
        this.dataBuilder = dataBuilder;
        this.statistic = statistic;
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
