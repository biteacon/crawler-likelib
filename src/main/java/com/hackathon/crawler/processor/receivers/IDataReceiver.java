package com.hackathon.crawler.processor.receivers;

public interface IDataReceiver {

    String getNodeData();
    String getBlockDataByHash(String hash);
    String getBlockDataByNumber(Long number);
    String getTopBlockHash();
    String getTransactionData(String hash);
    String getTransactionStatusData(String hash);
    String getAccountData(String address);

}
