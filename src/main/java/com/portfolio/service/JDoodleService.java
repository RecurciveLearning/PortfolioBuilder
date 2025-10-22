package com.portfolio.service;
import com.portfolio.config.Judge0Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

@Service
public class JDoodleService {

    @Value("${jdoodle.client-id}")
    private String clientId;

    @Value("${jdoodle.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public JDoodleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> executeCode(String script, String language, String versionIndex, String stdin) {
        String url = "https://api.jdoodle.com/v1/execute";

        Map<String, Object> request = new HashMap<>();
        request.put("clientId", clientId);
        request.put("clientSecret", clientSecret);
        request.put("script", script);
        request.put("language", language);
        request.put("versionIndex", versionIndex);
        request.put("stdin", stdin == null ? "" : stdin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }
}


