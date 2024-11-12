package com.sap.coordinatedhttprequests.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service responsible for coordinating HTTP requests based on the task's requirements.
 */
@Service
public class HttpRequestCoordinatorService {

    private final HttpService httpService;

    // Constructor injection for HttpService
    public HttpRequestCoordinatorService(HttpService httpService) {
        this.httpService = httpService;
    }

    /**
     * Executes coordinated HTTP requests.
     *
     * @param urls the list of URLs to call
     */
    public void executeHttpRequests(List<String> urls) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);

            // Every 5th request should wait for the previous ones to complete
            if ((i + 1) % 5 == 0) {

                // Wait for all previous futures to complete
                CompletableFuture<Void> dependentFuture = CompletableFuture
                        .allOf(futures.toArray(new CompletableFuture[0]))
                        .thenCompose(ignored -> httpService.makeRequest(url));

                futures.add(dependentFuture);
            } else {

                // Perform request without waiting
                CompletableFuture<Void> currentFuture = httpService.makeRequest(url);
                futures.add(currentFuture);
            }
        }

        // Wait for all requests to complete
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .join();
    }
}

