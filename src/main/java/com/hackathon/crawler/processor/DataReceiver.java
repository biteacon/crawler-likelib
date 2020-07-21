package com.hackathon.crawler.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hackathon.crawler.processor.exceptions.ErrorCode;
import com.hackathon.crawler.processor.exceptions.ProcessException;
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
public class DataReceiver {

    @Value("${crawler.host}")
    private String url;

    private final RestTemplate restTemplate;
    private final Statistic statistic;

    public DataReceiver(RestTemplate restTemplate, Statistic statistic) {
        this.restTemplate = restTemplate;
        this.statistic = statistic;
    }

    public String getNodeData() {
        return doRequest("/get_node_info", "");
    }

    public String getBlockDataByHash(String hashInBase64) {
        return doRequest("/get_block", "{\"hash\": \"" + hashInBase64 + "\"}");
    }

    public String getBlockDataByNumber(Long number) {
        return doRequest("/get_block", "{\"number\": " + number + "}");
    }

    public String getTopBlockHash() {
        String result;

        try {
            JSONObject obj = new JSONObject(getNodeData());
            result = obj.getString("top_block_hash");
        } catch (JSONException e) {
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t get top block hash");
        }

        return result;
    }

    public Long getTopBlocHeight() {
        Long result;

        try {
            JSONObject obj = new JSONObject(getNodeData());
            result = obj.getLong("top_block_number");
        } catch (JSONException e) {
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t get top block hash");
        }

        return result;
    }

    public String getTransactionData(String hashInBase64) {
        return doRequest("/get_transaction", "{\"hash\": \"" + hashInBase64 + "\"}");
    }

    public String getTransactionStatusData(String hashInBase64) {
        return doRequest("/get_transaction_status", "{\"hash\": \"" + hashInBase64 + "\"}");
    }

    public String getAccountData(String address) {
        return doRequest("/get_account", "{\"address\": \"" + address + "\"}");
    }

    private String doRequest(String path, String body) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, requestHeaders);

        String response = restTemplate.exchange(
                url + path, HttpMethod.POST, entity, String.class).getBody();
        statistic.requestCountInc();

        String result;

        try {
            ObjectNode node = new ObjectMapper().readValue(response, ObjectNode.class);

            if (node.get("status").toString().equals("\"ok\"")) {
                result = node.get("result").toString();
            } else throw new ProcessException(ErrorCode.RECEIVE_DATA_EXCEPTION, "Request with status \"error\"");

        } catch (JsonProcessingException e) {
            throw new ProcessException(ErrorCode.PARSE_EXCEPTION, "Can`t parse main json");
        }

        return result;
    }
}
