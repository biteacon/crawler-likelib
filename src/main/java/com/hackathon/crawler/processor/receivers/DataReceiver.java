package com.hackathon.crawler.processor.receivers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataReceiver implements IDataReceiver{

    @Value("${crawler.host}")
    private String url;

    private final RestTemplate restTemplate;

    public DataReceiver(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getNodeData() {
        return doRequest("/get_node_info", "");
    }

    @Override
    public String getBlockDataByHash(String hashInBase64) {
        return doRequest("/get_block", "{\"hash\": \"" + hashInBase64 + "\"}");
    }

    @Override
    public String getBlockDataByNumber(Long number) {
        return doRequest("/get_block", "{\"number\": " + number + "}");
    }

    @Override
    public String getTopBlockHash() {
        String result = null;

        try {
            JSONObject obj = new JSONObject(getNodeData());
            result = obj.getJSONObject("result").getString("top_block_hash");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String getTransactionData(String hashInBase64) {
        return doRequest("/get_transaction", "{\"hash\": \"" + hashInBase64 + "\"}");
    }

    @Override
    public String getTransactionStatusData(String hashInBase64) {
        return doRequest("/get_transaction_status", "{\"hash\": \"" + hashInBase64 + "\"}");
    }

    @Override
    public String getAccountData(String address) {
        return doRequest("/get_account", "{\"address\": \"" + address + "\"}");
    }

    private String doRequest(String path, String body) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, requestHeaders);

        String response = restTemplate.exchange(
                url + path, HttpMethod.POST, entity, String.class).getBody();

        String result = null;

        try {
            ObjectNode node = new ObjectMapper().readValue(response, ObjectNode.class);
            result = node.get("result").toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
