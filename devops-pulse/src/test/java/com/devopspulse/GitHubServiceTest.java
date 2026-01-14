package com.devopspulse;

import com.devopspulse.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class GitHubServiceTest {

    private GitHubService gitHubService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        gitHubService = new GitHubService(restTemplate);
    }

    @Test
    void getUserInfo_ShouldReturnSuccessForValidUser() {
        // Arrange
        String username = "testuser";
        String mockResponse = """
            {
                "login": "testuser",
                "public_repos": 5,
                "followers": 10,
                "name": "Test User",
                "location": "Kyiv"
            }
            """;

        mockServer.expect(requestTo("https://api.github.com/users/" + username))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        // Act
        Map<String, Object> result = gitHubService.getUserInfo(username);

        // Assert
        assertThat(result.get("success")).isEqualTo(true);
        assertThat(result.get("username")).isEqualTo("testuser");
        assertThat(result.get("publicRepos")).isEqualTo(5);
        assertThat(result.get("name")).isEqualTo("Test User");

        mockServer.verify();
    }

    @Test
    void getUserInfo_ShouldHandleMissingOptionalFields() {
        // Arrange
        String username = "testuser";
        String mockResponse = """
            {
                "login": "testuser",
                "public_repos": 5,
                "followers": 10
            }
            """;

        mockServer.expect(requestTo("https://api.github.com/users/" + username))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        // Act
        Map<String, Object> result = gitHubService.getUserInfo(username);

        // Assert
        assertThat(result.get("success")).isEqualTo(true);
        assertThat(result.get("username")).isEqualTo("testuser");
        assertThat(result.get("name")).isEqualTo("Not specified");
        assertThat(result.get("location")).isEqualTo("Not specified");

        mockServer.verify();
    }

    @Test
    void getUserInfo_ShouldHandleNotFound() {
        // Arrange
        String username = "nonexistent";

        mockServer.expect(requestTo("https://api.github.com/users/" + username))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // Act
        Map<String, Object> result = gitHubService.getUserInfo(username);

        // Assert
        assertThat(result.get("success")).isEqualTo(false);
        assertThat(result.get("error")).isEqualTo("User not found");

        mockServer.verify();
    }

    @Test
    void getUserInfo_ShouldHandleRateLimit() {
        // Arrange
        String username = "testuser";

        mockServer.expect(requestTo("https://api.github.com/users/" + username))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        // Act
        Map<String, Object> result = gitHubService.getUserInfo(username);

        // Assert
        assertThat(result.get("success")).isEqualTo(false);
        assertThat(result.get("error")).isEqualTo("Rate limit exceeded");

        mockServer.verify();
    }
}