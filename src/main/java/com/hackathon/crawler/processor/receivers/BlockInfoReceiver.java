package com.hackathon.crawler.processor.receivers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockInfoReceiver extends DataReceiver {

    public BlockInfoReceiver(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public String receive(String hashInBase64) {
        return doRequest("/get_block", "{\"hash\": " + hashInBase64 + "}");
    }
}
