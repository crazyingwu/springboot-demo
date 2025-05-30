package com.example.demo.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Service
public class HttpService {

    @Autowired
    private RestTemplate restTemplate;

    private CloseableHttpClient httpClient;


    @PostConstruct
    public void init() {
        this.httpClient = HttpClients.custom()
                .build();
    }


    public String getExampleData() {
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        URIBuilder builder = null;
        try {
            builder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        HttpGet httpGet = new HttpGet(uri);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 检查HTTP状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("HTTP Error: " + statusCode);
            }

            // 获取响应实体并转换为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8); // 明确指定字符集
            } else {
                throw new RuntimeException("Response body is empty");
            }
        } catch (IOException e) {
            throw new RuntimeException("Request failed", e);
        }
    }
}