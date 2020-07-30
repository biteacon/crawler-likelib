package com.hackathon.crawler;

import com.hackathon.crawler.processor.cache.CacheTxFilesHandler;
import com.hackathon.crawler.processor.cache.TransactionCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class CrawlerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CrawlerApplication.class, args);
		TransactionCache transactionCache = (TransactionCache) context.getBean("transactionCache");
		CacheTxFilesHandler cacheTxFilesHandler = (CacheTxFilesHandler) context.getBean("cacheTxFilesHandler");
		byte[] cacheInBytes = cacheTxFilesHandler.readCacheFromFile();
		transactionCache.fillCacheFromBytes(cacheInBytes);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
