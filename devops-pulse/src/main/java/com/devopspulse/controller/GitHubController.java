package com.devopspulse.controller;

import com.devopspulse.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class GitHubController {
    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }


    @GetMapping("/github/{username}")
    public Map<String, Object> getGithubInfo(@PathVariable String username){
        return gitHubService.getUserInfo(username);
    }
}
