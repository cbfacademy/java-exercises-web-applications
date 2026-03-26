package com.cbfacademy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

@DisplayName("HttpRequestBuilder Tests")
public class HttpRequestBuilderTest {

    @Test
    @DisplayName("Should use HttpRequest.newBuilder() to construct the request")
    void shouldCallHttpRequestNewBuilder() {
        String testUrl = "https://example.com";

        try (MockedStatic<HttpRequest> httpRequestMock = mockStatic(HttpRequest.class)) {
            httpRequestMock.when(HttpRequest::newBuilder).thenCallRealMethod();
            httpRequestMock.when(() -> HttpRequest.newBuilder(any(URI.class))).thenCallRealMethod();
            HttpRequestBuilder.build(testUrl);

            httpRequestMock.verify(HttpRequest::newBuilder, times(1));
        }
    }

    @Test
    @DisplayName("Should build HttpRequest with correct method")
    void shouldBuildRequestWithGetMethod() {
        String testUrl = "https://example.com";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        assertEquals("GET", request.method(), "Request method should be GET");
    }

    @Test
    @DisplayName("Should build HttpRequest with correct URI")
    void shouldBuildRequestWithCorrectUri() {
        String testUrl = "https://example.com/api/data";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        assertEquals(URI.create(testUrl), request.uri(), "Request URI should match the provided URL");
    }

    @Test
    @DisplayName("Should build HttpRequest with HTTP_1_1 version")
    void shouldBuildRequestWithHttp11Version() {
        String testUrl = "https://example.com";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        Optional<HttpClient.Version> version = request.version();
        assertTrue(version.isPresent(), "HTTP version should be present");
        assertEquals(HttpClient.Version.HTTP_1_1, version.get(), "HTTP version should be HTTP_1_1");
    }

    @Test
    @DisplayName("Should build HttpRequest with correct User-Agent header")
    void shouldBuildRequestWithCorrectUserAgent() {
        String testUrl = "https://example.com";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        Optional<String> userAgent = request.headers().firstValue("User-Agent");
        assertTrue(userAgent.isPresent(), "User-Agent header should be present");
        assertEquals("Mozilla/5.0 (Java Exercise Client)", userAgent.get(), 
                "User-Agent should match expected value");
    }

    @Test
    @DisplayName("Should build HttpRequest with correct Accept header")
    void shouldBuildRequestWithCorrectAcceptHeader() {
        String testUrl = "https://example.com";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        Optional<String> accept = request.headers().firstValue("Accept");
        assertTrue(accept.isPresent(), "Accept header should be present");
        assertEquals("text/html,application/json,*/*;q=0.8", accept.get(), 
                "Accept header should match expected value");
    }

    @Test
    @DisplayName("Should build HttpRequest with 30 second timeout")
    void shouldBuildRequestWithCorrectTimeout() {
        String testUrl = "https://example.com";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        Optional<Duration> timeout = request.timeout();
        assertTrue(timeout.isPresent(), "Timeout should be present");
        assertEquals(Duration.ofSeconds(30), timeout.get(), "Timeout should be 30 seconds");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://www.google.com",
        "https://api.github.com/users",
        "https://httpbin.org/get",
        "https://example.com/path/to/resource?param=value"
    })
    @DisplayName("Should handle various URL formats correctly")
    void shouldHandleVariousUrlFormats(String url) {
        HttpRequest request = HttpRequestBuilder.build(url);

        assertNotNull(request, "Request should not be null");
        assertEquals(URI.create(url), request.uri(), "Request URI should match input URL");
        assertEquals("GET", request.method(), "Method should always be GET");
    }

    @Test
    @DisplayName("Should build request with all required headers present")
    void shouldBuildRequestWithAllRequiredHeaders() {
        String testUrl = "https://example.com";
        HttpRequest request = HttpRequestBuilder.build(testUrl);

        // Verify that both required headers are present
        assertTrue(request.headers().firstValue("User-Agent").isPresent(), 
                "User-Agent header should be present");
        assertTrue(request.headers().firstValue("Accept").isPresent(), 
                "Accept header should be present");

        // Verify no restricted headers are set (Connection header should not be manually set)
        assertFalse(request.headers().firstValue("Connection").isPresent(), 
                "Connection header should not be manually set");
    }

    @Test
    @DisplayName("Should create different request instances for different URLs")
    void shouldCreateDifferentRequestInstances() {
        String url1 = "https://example.com/path1";
        String url2 = "https://example.com/path2";

        HttpRequest request1 = HttpRequestBuilder.build(url1);
        HttpRequest request2 = HttpRequestBuilder.build(url2);

        assertNotSame(request1, request2, "Different requests should be separate instances");
        assertNotEquals(request1.uri(), request2.uri(), "Different requests should have different URIs");
    }
} 