package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.model.Login;
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
      // Login Service for updating DB
      loginService.processLogin(authentication, model);
      // Retrieving upadate Login
      Login login = loginService.getLogin(user.getAttribute("email"));
      // Setting model
      model.addAttribute("name", login.getName());
      model.addAttribute("picture", login.getPicture());
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
