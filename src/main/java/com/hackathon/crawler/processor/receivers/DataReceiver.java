package com.hackathon.crawler.processor.receivers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class DataReceiver {

    @Value("${downloader.host}")
    private String url;

    private final RestTemplate restTemplate;

    public DataReceiver(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    abstract String receive(String param);


    protected String doRequest(String path, String body) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, requestHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                url + path, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

}
