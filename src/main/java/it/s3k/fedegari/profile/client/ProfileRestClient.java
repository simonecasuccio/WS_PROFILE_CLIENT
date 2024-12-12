package it.s3k.fedegari.profile.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import it.s3k.fedegari.profile.exception.ProfileApiException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Component
@Slf4j
public class ProfileRestClient {
    
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String username;
    private final String password;
    
    public ProfileRestClient(
            @Value("${profile.base-url}") String baseUrl,
            @Value("${profile.username}") String username,
            @Value("${profile.password}") String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    }

    public String executeRequest(String path, String method, String body) throws Exception {
        String url = baseUrl + path;
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", getBasicAuthHeader());

        switch (method.toUpperCase()) {
            case "GET":
                requestBuilder.GET();
                break;
            case "POST":
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;
            case "PUT":
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
                break;
            case "DELETE":
                requestBuilder.DELETE();
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        HttpResponse<String> response = httpClient.send(
            requestBuilder.build(),
            HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() >= 400) {
            throw new ProfileApiException(
                "Profile API request failed with status " + response.statusCode(),
                response.statusCode(),
                response.body()
            );
        }

        return response.body();
    }

    private String getBasicAuthHeader() {
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}