package com.devopspulse.controller;

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

    @GetMapping("/github/{username}")
    public Map<String, Object> getGithubInfo(@PathVariable String username){
        Map<String, Object> githubInfo = new HashMap<>();
        githubInfo.put("username", username);
        githubInfo.put("publicRepos", 1);
        githubInfo.put("followers", 0);
        githubInfo.put("following", 0);
        githubInfo.put("accountCreatedAt", "2023-01-01");
        return githubInfo;
    }
}
