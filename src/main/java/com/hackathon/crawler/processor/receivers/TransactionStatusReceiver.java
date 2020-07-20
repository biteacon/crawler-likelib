package com.hackathon.crawler.processor.receivers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionStatusReceiver extends DataReceiver {

    public TransactionStatusReceiver(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public String receive(String hashInBase64) {
        return doRequest("/get_transaction_status", "{\"hash\": " + hashInBase64 + "}");
    }
}
