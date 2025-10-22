package com.portfolio.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Judge0Config {

    @Value("${judge0.base-url}")
    private String baseUrl;

    @Value("${judge0.api-host}")
    private String apiHost;

    @Value("${judge0.api-key}")
    private String apiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiHost() {
        return apiHost;
    }

    public String getApiKey() {
        return apiKey;
    }
}
