package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.service.LoginService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {
  private final LoginService loginService;

  public LandingController(LoginService loginService) {
    this.loginService = loginService;
  }

  // Home page requiring authentication
  @GetMapping("/")
  public String showHomePage(Model model, OAuth2AuthenticationToken authentication) {
    if (authentication != null) {
      // You can add user details to the model if needed
      OAuth2User user = authentication.getPrincipal();
      String name = user.getAttribute("name");
      model.addAttribute("name", name);
      String picture = user.getAttribute("picture");
      model.addAttribute("picture", picture);
      // Login Service for updating DB
      loginService.processLogin(authentication, model);
    }
    return "index";
  }

  @GetMapping("/landing")
  public String showLandingPage() {
    return "landing";
  }

  @GetMapping("/login-modal")
  public String showLoginModal(Model model, OAuth2AuthenticationToken authentication) {
    return "login-modal";
  }
}
