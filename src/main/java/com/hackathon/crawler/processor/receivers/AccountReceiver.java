package com.hackathon.crawler.processor.receivers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountReceiver extends DataReceiver {

    public AccountReceiver(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public String receive(String address) {
        return doRequest("/get_account", "{\"address\": " + address + "}");
    }
}
