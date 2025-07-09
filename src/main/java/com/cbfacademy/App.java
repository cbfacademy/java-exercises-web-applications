package com.cbfacademy;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class App {

    /**
     * Main method demonstrating both HTTP exercises working together
     */
    public static void main(String[] args) {
        System.out.println("Web Applications - HTTP Exercises Demo");
        System.out.println("=====================================\n");

        try {
            // Task 1: Build HTTP Request using HttpRequestBuilder
            System.out.println("Task 1: Building HTTP Request");
            System.out.println("---------------------------------");
            
            String testUrl = "https://httpbin.org/get";
            HttpRequest request = HttpRequestBuilder.build(testUrl);
            
            System.out.println("Method: " + request.method());
            System.out.println("URI: " + request.uri());
            System.out.println("HTTP Version: " + request.version().orElse(HttpClient.Version.HTTP_1_1));
            System.out.println("Headers: " + request.headers().map());
            System.out.println();

            // Task 2: Make request and parse response using HttpResponseParser
            System.out.println("Task 2: Making Request and Parsing Response");
            System.out.println("----------------------------------------------");
            
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Parse the response using HttpResponseParser and display the results
            Map<String, String> responseData = HttpResponseParser.parse(response);
            
            System.out.println("=== HTTP Response Analysis ===");
            System.out.println("Request URL: " + responseData.get("URL"));
            System.out.println("Status Code: " + responseData.get("Status"));
            if (responseData.containsKey("Server")) {
                System.out.println("Server: " + responseData.get("Server"));
            }
            if (responseData.containsKey("Content-Type")) {
                System.out.println("Content-Type: " + responseData.get("Content-Type"));
            }
            if (responseData.containsKey("Content-Length")) {
                System.out.println("Content-Length: " + responseData.get("Content-Length"));
            }
            System.out.println("================================");
            
            // Optional: Show part of the response body
            System.out.println("\nResponse Body Preview:");
            String body = response.body();
            if (body.length() > 200) {
                System.out.println(body.substring(0, 200) + "...");
            } else {
                System.out.println(body);
            }

        } catch (Exception e) {
            System.err.println("Error during HTTP communication: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 