package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> result = new HashMap<>();

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                result.put("success", false);
                result.put("error", "JDoodle execution limit reached. Please wait before trying again.");
            }
            else if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                // ✅ Debugging log (optional)
                System.out.println("🔹 JDoodle API Response: " + body);

                Object errorValue = body.get("error");
                if (errorValue != null && errorValue.toString().toLowerCase().contains("quota")) {
                    result.put("success", false);
                    result.put("error", "JDoodle execution quota exceeded. Upgrade your plan or wait.");
                }
                else if (body.get("output") != null) {
                    result.put("success", true);
                    result.put("output", body.get("output"));
                    result.put("data", body);
                }
                else {
                    result.put("success", false);
                    result.put("error", "No output received from JDoodle. Check your code or language settings.");
                }
            }
            else {
                result.put("success", false);
                result.put("error", "JDoodle API returned non-OK status: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.TooManyRequests ex) {
            result.put("success", false);
            result.put("error", "JDoodle execution limit reached. Please try later.");
        } catch (HttpClientErrorException ex) {
            result.put("success", false);
            result.put("error", "Client error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            result.put("success", false);
            result.put("error", "Error connecting to JDoodle API: " + ex.getMessage());
        } catch (Exception ex) {
            result.put("success", false);
            result.put("error", "Unexpected error: " + ex.getMessage());
        }

        // ✅ Log final result for clarity
        System.out.println("✅ Final JDoodleService Result: " + result);

        return result;
    }
}
