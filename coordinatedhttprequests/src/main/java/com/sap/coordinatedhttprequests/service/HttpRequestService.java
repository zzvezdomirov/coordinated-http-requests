package com.sap.coordinatedhttprequests.service;

import com.sap.coordinatedhttprequests.exception.HttpRequestFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * A Spring service for making HTTP requests asynchronously.
 */
@Service
public class HttpRequestService {
    private final RestTemplate restTemplate;

    // Constructor injection for RestTemplate
    public HttpRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends an HTTP GET request asynchronously to the given URL.
     *
     * @param url the URL to which the request is sent
     * @return a CompletableFuture that completes when the request finishes
     */
    public CompletableFuture<Void> makeRequest(String url) {
        return CompletableFuture.runAsync(() -> {

            // Perform an HTTP GET request
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Throw a custom runtime exception if the status code is not 200 OK
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new HttpRequestFailedException(
                        response.getStatusCode().value(),
                        String.format(
                                "HTTP request failed with status code: %d",
                                response.getStatusCode().value()
                        )
                );
            }
        });
    }
}
