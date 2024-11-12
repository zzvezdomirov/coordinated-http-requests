package com.sap.coordinatedhttprequests.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service responsible for coordinating HTTP requests based on the task's requirements.
 */
@Service
@Log4j2
public class HttpRequestCoordinatorService {

    private final HttpRequestService httpRequestService;

    // Constructor injection for HttpService
    public HttpRequestCoordinatorService(HttpRequestService httpRequestService) {
        this.httpRequestService = httpRequestService;
    }

    /**
     * Executes coordinated HTTP requests.
     *
     * @param urls the list of URLs to call
     */
    public String executeHttpRequests(List<String> urls) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        log.info("Starting execution of HTTP requests");

        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            log.debug("Processing request to URL: {}", url);

            // Every 5th request should wait for the previous ones to complete
            if ((i + 1) % 5 == 0) {
                log.debug("Waiting for previous requests to complete");
                // Wait for all previous futures to complete
                CompletableFuture<Void> dependentFuture = CompletableFuture
                        .allOf(futures.toArray(new CompletableFuture[0]))
                        .thenCompose(ignored -> httpRequestService.makeRequest(url));

                futures.add(dependentFuture);
            } else {
                // Perform request without waiting
                CompletableFuture<Void> currentFuture = httpRequestService.makeRequest(url);
                futures.add(currentFuture);
            }
        }

        // Wait for all requests to complete
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .join();
        log.info("All HTTP requests completed successfully");

        return "Requests processed successfully.";
    }
}

