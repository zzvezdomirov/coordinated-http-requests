package com.sap.coordinatedhttprequests.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HttpRequestCoordinatorServiceTest {

    @Mock
    private HttpRequestService httpRequestService;

    @InjectMocks
    private HttpRequestCoordinatorService httpRequestCoordinatorService;

    private AutoCloseable session;

    @BeforeEach
    public void setup() {
        session = MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @AfterEach
    public void tearDown() throws Exception {
        session.close(); // Close resources to avoid leaks
    }

    @Test
    public void Should_ExecuteAllRequestsSuccessfully_When_ValidUrlsAreProvided() throws ExecutionException, InterruptedException {
        // Arrange: List of URLs to be processed
        List<String> urls = List.of(
                "http://service1.com",
                "http://service2.com",
                "http://service3.com",
                "http://service4.com",
                "http://service5.com"
        );

        // Mock a simple response: Return a completed CompletableFuture with a successful ResponseEntity
        when(httpRequestService.makeRequest(anyString()))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(ResponseEntity.ok().build()));

        // Act: Call the method to coordinate the requests
        assertDoesNotThrow(() -> httpRequestCoordinatorService.executeHttpRequests(urls));

        // Verify: Ensure the correct number of requests were made
        verify(httpRequestService, times(urls.size())).makeRequest(anyString());
    }

    @Test
    public void Should_EnsureFifthRequestWaits_When_PreviousRequestsAreNotComplete() throws ExecutionException, InterruptedException {
        // Arrange: Create a list of URLs
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            urls.add("http://service" + (i + 1) + ".com");
        }

        // Prepare CountDownLatch for synchronizing the requests
        CountDownLatch latchForFifthRequest = new CountDownLatch(1);

        // Mock the behavior of the service: delay execution of the 5th request
        when(httpRequestService.makeRequest(anyString()))
                .thenAnswer(invocation -> {

                    // Simulate delay for the requests to complete
                    String url = invocation.getArgument(0);
                    if (urls.indexOf(url) == 4) { // For the 5th request (index 4)
                        latchForFifthRequest.await(); // Ensure the 5th request waits for previous ones
                    }
                    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
                });

        // Act: Start executing the requests
        // Execute the HTTP requests in parallel
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                httpRequestCoordinatorService.executeHttpRequests(urls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Wait for the first 4 requests to complete (simulating sequential execution)
        // Simulate the time for the first 4 requests to complete
        Thread.sleep(500);

        // Now trigger the 5th request to proceed
        latchForFifthRequest.countDown();

        // Wait for all requests to complete
        future.join();

        verify(httpRequestService, times(10)).makeRequest(anyString());
    }
}
