package com.devopspulse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GitHubService {
    private final RestTemplate restTemplate;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("User-Agent", "DevOps-Pulse-App/1.0.0");
            return execution.execute(request, body);
        });
    }

    public Map<String, Object> getUserInfo(String username) {
        String url = "https://api.github.com/users/" + username;

        try {
            Map<String, Object> gitHubInfo = restTemplate.getForObject(url, Map.class);
            return createSuccessResponse(gitHubInfo);

        } catch (HttpClientErrorException.NotFound e) {
            return createNotFoundResponse(username);

        } catch (HttpClientErrorException.TooManyRequests e) {
            return createErrorResponse("Rate limit exceeded",
                    "GitHub API rate limit reached. Try again in an hour.");

        } catch (Exception e) {
            return createErrorResponse("GitHub API error", e.getMessage());
        }
    }

    private Map<String, Object> createSuccessResponse(Map<String, Object> gitHubInfo) {
        Map<String, Object> response = new HashMap<>();

        response.put("username", gitHubInfo.get("login"));
        response.put("publicRepos", gitHubInfo.get("public_repos"));
        response.put("followers", gitHubInfo.get("followers"));

        response.put("name", gitHubInfo.getOrDefault("name", "Not specified"));
        response.put("location", gitHubInfo.getOrDefault("location", "Not specified"));

        response.put("success", true);
        return response;
    }

    private Map<String, Object> createNotFoundResponse(String username) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "User not found");
        error.put("message", "GitHub user '" + username + "' does not exist");
        error.put("success", false);
        return error;
    }

    private Map<String, Object> createErrorResponse(String errorType, String details) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", errorType);
        error.put("message", details);
        error.put("success", false);
        return error;
    }
}