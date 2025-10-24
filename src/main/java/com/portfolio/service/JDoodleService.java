package com.portfolio.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class JDoodleService {

    private static final Logger log = LoggerFactory.getLogger(JDoodleService.class);

    private final RestTemplate restTemplate;

    @Value("${jdoodle.client-id}")
    private String clientId;

    @Value("${jdoodle.client-secret}")
    private String clientSecret;

    public JDoodleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> executeCode(String script, String language, String versionIndex, String stdin) {
        Map<String, Object> result = new HashMap<>();
        String url = "https://api.jdoodle.com/v1/execute";

        Map<String, Object> payload = Map.of(
                "clientId", clientId,
                "clientSecret", clientSecret,
                "script", script,
                "language", language,
                "versionIndex", versionIndex,
                "stdin", stdin == null ? "" : stdin
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        int retries = 3; // retry 3 times for transient errors
        for (int attempt = 1; attempt <= retries; attempt++) {
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String,Object> body = response.getBody();
                    result.put("success", true);
                    result.put("output", body.get("output"));
                    result.put("cpuTime", body.get("cpuTime"));
                    result.put("memory", body.get("memory"));
                    log.info("JDoodle execution successful");
                    return result;
                } else {
                    result.put("success", false);
                    result.put("error", "JDoodle returned non-OK status: " + response.getStatusCode());
                }

            } catch (ResourceAccessException ex) {
                log.warn("Network/SSL error on attempt {}: {}", attempt, ex.getMessage());
                if (attempt == retries) {
                    result.put("success", false);
                    result.put("error", "Network error connecting to JDoodle. Check SSL & internet connectivity.");
                }
            } catch (HttpClientErrorException ex) {
                result.put("success", false);
                result.put("error", "JDoodle client error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                break;
            } catch (HttpServerErrorException ex) {
                log.warn("JDoodle server error on attempt {}: {}", attempt, ex.getStatusCode());
                if (attempt == retries) {
                    result.put("success", false);
                    result.put("error", "JDoodle server error: " + ex.getStatusCode());
                }
            } catch (RestClientException ex) {
                log.error("JDoodle API error: {}", ex.getMessage());
                result.put("success", false);
                result.put("error", "JDoodle API connection error: " + ex.getMessage());
            } catch (Exception ex) {
                log.error("Unexpected error executing code: {}", ex.getMessage());
                result.put("success", false);
                result.put("error", "Unexpected error: " + ex.getMessage());
            }

            // exponential backoff before retry
            try { Thread.sleep(1000L * attempt); } catch (InterruptedException ignored) {}
        }

        return result;
    }
}
