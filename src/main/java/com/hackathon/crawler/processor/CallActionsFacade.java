package com.hackathon.crawler.processor;

import com.hackathon.crawler.data.entities.Block;
import com.hackathon.crawler.data.entities.Account;
import com.hackathon.crawler.processor.actions.BlockInformationReceiver;
import com.hackathon.crawler.processor.actions.InformationBuilder;
import com.hackathon.crawler.processor.actions.InformationWriter;
import org.springframework.stereotype.Component;

@Component
public class CallActionsFacade {

    private final BlockInformationReceiver blockInformationReceiver;
    private final InformationBuilder informationBuilder;
    private final InformationWriter informationWriter;

    public CallActionsFacade(BlockInformationReceiver blockInformationReceiver,
                             InformationBuilder informationBuilder,
                             InformationWriter informationWriter) {
        this.blockInformationReceiver = blockInformationReceiver;
        this.informationBuilder = informationBuilder;
        this.informationWriter = informationWriter;
    }

    protected void callStandardScript() {

    }
}
