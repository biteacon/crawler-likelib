package com.hackathon.crawler.processor.actions;

import com.hackathon.crawler.data.entities.Block;
import com.hackathon.crawler.data.entities.Account;
import org.springframework.stereotype.Component;

@Component
public class InformationBuilder {

    public Account doAction(Block blockInfo) {




        return new Account();
    }
}
