package com.sap.coordinatedhttprequests.controller;


import com.sap.coordinatedhttprequests.service.HttpRequestCoordinatorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling coordinated HTTP request operations.
 */
@RestController
@Log4j2
public class CoordinatedHttpRequestsController {

    private final HttpRequestCoordinatorService httpRequestCoordinatorService;

    // Constructor injection for HttpRequestCoordinatorService
    public CoordinatedHttpRequestsController(HttpRequestCoordinatorService httpRequestCoordinatorService) {
        this.httpRequestCoordinatorService = httpRequestCoordinatorService;
    }

    /**
     * Endpoint for triggering coordinated HTTP requests.
     *
     * @param urls the list of URLs to call
     */
    @PostMapping("/execute-requests")
    public ResponseEntity<String> executeRequests(@RequestBody List<String> urls) {
        log.info("Received request to execute HTTP requests.");

        try {
            String result = httpRequestCoordinatorService.executeHttpRequests(urls);
            log.info("HTTP requests executed successfully.");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error during HTTP request execution: ", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing requests");
        }
    }
}
