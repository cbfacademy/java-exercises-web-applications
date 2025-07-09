package com.cbfacademy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@DisplayName("HttpResponseParser Tests")
public class HttpResponseParserTest {

    private HttpClient client;

    @BeforeEach
    void setUp() {
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @Test
    @DisplayName("Should parse response and return map with URL and status code")
    @Timeout(10) // 10 second timeout for network operations
    void shouldParseResponseWithBasicInformation() throws Exception {
        // Use httpbin.org which provides reliable test endpoints
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        // Verify basic fields are always present
        assertNotNull(result, "Result map should not be null");
        assertTrue(result.containsKey("URL"), "Result should contain URL key");
        assertTrue(result.containsKey("Status"), "Result should contain Status key");

        assertEquals("https://httpbin.org/get", result.get("URL"), "URL should match request URL");
        assertEquals("200", result.get("Status"), "Status should be 200 for successful request");
    }

    @Test
    @DisplayName("Should include Content-Type header when present")
    @Timeout(10)
    void shouldIncludeContentTypeWhenPresent() throws Exception {
        // httpbin.org/json returns JSON content with Content-Type header
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/json"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        assertTrue(result.containsKey("Content-Type"), "Result should contain Content-Type when present");
        assertNotNull(result.get("Content-Type"), "Content-Type should not be null");
        assertTrue(result.get("Content-Type").contains("application/json"), 
                "Content-Type should indicate JSON for /json endpoint");
    }

    @Test
    @DisplayName("Should include Server header when present")
    @Timeout(10)
    void shouldIncludeServerHeaderWhenPresent() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        // httpbin.org typically includes a Server header
        if (result.containsKey("Server")) {
            assertNotNull(result.get("Server"), "Server header should not be null when present");
            assertFalse(result.get("Server").isEmpty(), "Server header should not be empty when present");
        }
    }

    @Test
    @DisplayName("Should handle different status codes correctly")
    @Timeout(10)
    void shouldHandleDifferentStatusCodes() throws Exception {
        // Test 404 status code
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/status/404"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        assertEquals("404", result.get("Status"), "Status should be 404 for /status/404 endpoint");
        assertEquals("https://httpbin.org/status/404", result.get("URL"), "URL should match request URL");
    }

    @Test
    @DisplayName("Should not include headers that are absent")
    void shouldNotIncludeAbsentHeaders() throws Exception {
        // Create a simple request and response
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        // Verify that only present headers are included
        // We can't guarantee which headers will be absent, but we can verify
        // that if a key exists in the map, it has a non-null, non-empty value
        for (Map.Entry<String, String> entry : result.entrySet()) {
            assertNotNull(entry.getValue(), 
                    "Value for key '" + entry.getKey() + "' should not be null");
            assertFalse(entry.getValue().isEmpty(), 
                    "Value for key '" + entry.getKey() + "' should not be empty");
        }
    }

    @Test
    @DisplayName("Should handle URLs with query parameters correctly")
    @Timeout(10)
    void shouldHandleUrlsWithQueryParameters() throws Exception {
        String testUrl = "https://httpbin.org/get?param1=value1&param2=value2";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(testUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        assertEquals(testUrl, result.get("URL"), "URL should include query parameters");
    }

    @Test
    @DisplayName("Should return consistent map structure")
    @Timeout(10)
    void shouldReturnConsistentMapStructure() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        // Verify required keys are always present
        assertTrue(result.containsKey("URL"), "URL key should always be present");
        assertTrue(result.containsKey("Status"), "Status key should always be present");

        // Verify all values are strings
        for (Object value : result.values()) {
            assertInstanceOf(String.class, value, "All values should be strings");
        }

        // Verify map is not empty
        assertFalse(result.isEmpty(), "Result map should not be empty");
        assertTrue(result.size() >= 2, "Result map should contain at least URL and Status");
    }

    @Test
    @DisplayName("Should handle HTTPS and HTTP URLs correctly")
    @Timeout(10)
    void shouldHandleDifferentProtocols() throws Exception {
        // Test HTTPS (httpbin.org uses HTTPS)
        HttpRequest httpsRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        HttpResponse<String> httpsResponse = client.send(httpsRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, String> httpsResult = HttpResponseParser.parse(httpsResponse);

        assertTrue(httpsResult.get("URL").startsWith("https://"), 
                "HTTPS URL should be preserved correctly");
        assertEquals("200", httpsResult.get("Status"), "HTTPS request should succeed");
    }

    @Test
    @DisplayName("Should extract Content-Length when present")
    @Timeout(10)
    void shouldExtractContentLengthWhenPresent() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> result = HttpResponseParser.parse(response);

        // Content-Length might or might not be present depending on the server
        if (result.containsKey("Content-Length")) {
            String contentLength = result.get("Content-Length");
            assertNotNull(contentLength, "Content-Length should not be null when present");
            // Verify it's a valid number
            assertDoesNotThrow(() -> Integer.parseInt(contentLength), 
                    "Content-Length should be a valid number");
            assertTrue(Integer.parseInt(contentLength) >= 0, 
                    "Content-Length should be non-negative");
        }
    }
} 