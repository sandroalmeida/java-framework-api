package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.service.LoginService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final LoginService loginService;

    public HomeController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String index(Model model, OAuth2AuthenticationToken authentication) {
        // Delegate login processing to the service
        loginService.processLogin(authentication, model);
        return "index"; // Render the index.html template
    }
}
