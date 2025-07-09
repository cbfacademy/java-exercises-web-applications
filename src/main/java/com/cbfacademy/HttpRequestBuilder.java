package com.cbfacademy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

/**
 * Exercise 1: HTTP Request Builder
 * A utility class for constructing HttpRequest objects with specific properties.
 */
public class HttpRequestBuilder {
    
    /**
     * Builds an HttpRequest object with predefined properties.
     * Method: GET, HTTP Version: HTTP_1_1, User-Agent: Mozilla/5.0 (Java Exercise Client),
     * Accept: text/html,application/json,star/star;q=0.8, Timeout: 30 seconds
     * 
     * @param url The URL for the HTTP request
     * @return HttpRequest object configured with the specified properties
     * @throws RuntimeException if the request cannot be built
     */
    public static HttpRequest build(String url) {
        try {
            return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_1_1)
                .header("User-Agent", "Mozilla/5.0 (Java Exercise Client)")
                .header("Accept", "text/html,application/json,*/*;q=0.8")
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build HTTP request for URL: " + url, e);
        }
    }
} 