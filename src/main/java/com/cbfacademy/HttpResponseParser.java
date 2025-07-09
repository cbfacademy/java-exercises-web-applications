package com.cbfacademy;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A utility class for parsing HttpResponse objects.
 */
public class HttpResponseParser {
    
    /**
     * Parses the HttpResponse object and returns a map containing key information:
     * - "URL": Response URL
     * - "Status": HTTP status code as a string
     * - "Server": Server header value (if present)
     * - "Content-Type": Content-Type header value (if present)
     * - "Content-Length": Content-Length header value (if present)
     * 
     * @param response The HttpResponse object to parse
     * @return Map containing the extracted response information
     */
    public static Map<String, String> parse(HttpResponse<String> response) {
        Map<String, String> responseData = new HashMap<>();
        
        responseData.put("URL", response.request().uri().toString());
        responseData.put("Status", String.valueOf(response.statusCode()));
        addHeader(response, "Server", responseData);
        addHeader(response, "Content-Type", responseData);
        addHeader(response, "Content-Length", responseData);
        
        return responseData;
    }
    
    /**
     * Helper method to add a response header value to the response data map, if present
     * 
     * @param response The HttpResponse object
     * @param headerName The name of the header to extract
     * @param responseData The map to add the header value to
     */
    private static void addHeader(HttpResponse<String> response, String headerName, Map<String, String> responseData) {
        Optional<String> headerValue = response.headers().firstValue(headerName);
        if (headerValue.isPresent()) {
            responseData.put(headerName, headerValue.get());
        }
    }
} 