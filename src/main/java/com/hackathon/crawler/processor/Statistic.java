package com.hackathon.crawler.processor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Statistic {

    private Long requestCount = 0L;
    private Long txInCache = 0L;

    private Long blockCount = 0L;
    private Long txCount = 0L;

    public void requestCountInc() {
        requestCount++;
    }

    public void setTxInCacheInc() {
        txInCache++;
    }

    public void setBlockCountInc() {
        blockCount++;
    }

    public void setTxCountInc() {
        txCount++;
    }


    public void refreshRequestCount() {
        requestCount = 0L;
        blockCount = 0L;
        txCount = 0L;
    }

    public String toString(long start, long finish) {

        float durationTime = (float) (finish - start)/1000;
        String formattedDurationTime = String.format("%.2f", durationTime);

        return "Session complete!" + "  Duration: " + formattedDurationTime + " sec" + "\n"
                + "Called requests: " + requestCount + ", transactions in cache: " + txInCache + "\n"
                + "Blocks added: " + blockCount + ", transactions added: " + txCount;
    }
}
