package com.hackathon.crawler.scheduler;

import com.hackathon.crawler.processor.Processor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GetDataScheduler {

    private final Processor processor;

    public GetDataScheduler(Processor processor) {
        this.processor = processor;
    }

    @Scheduled(fixedDelayString = "${crawler.sync-time}", initialDelay = 10000)
    public void callDownloader() {
        processor.doScript();
    }

}
