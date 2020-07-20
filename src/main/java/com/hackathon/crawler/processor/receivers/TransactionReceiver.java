package com.hackathon.crawler.processor.receivers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionReceiver extends DataReceiver {

    public TransactionReceiver(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public String receive(String hashInBase64) {
        return doRequest("/get_transaction", "{\"hash\": " + hashInBase64 + "}");
    }
}
