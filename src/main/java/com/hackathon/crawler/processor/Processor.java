package com.hackathon.crawler.processor;

import org.springframework.stereotype.Service;

@Service
public class Processor implements IProcessor {

    private final CallActionsFacade callActionsFacade;

    public Processor(CallActionsFacade callActionsFacade) {
        this.callActionsFacade = callActionsFacade;
    }

    @Override
    public void doScript() {
        callActionsFacade.callStandardScript();
    }

}
