package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.model.Login;
import com.sandrocorrea.java_framework_api.repository.LoginRepository;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final LoginRepository loginRepository;

  public HomeController(LoginRepository loginRepository) {
    this.loginRepository = loginRepository;
  }

  @GetMapping("/")
  public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
    if (user != null) {
      model.addAttribute("name", user.getAttribute("name"));

      // Extract email and other user attributes
      String email = user.getAttribute("email");
      String name = user.getAttribute("name");
      String givenName = user.getAttribute("given_name");
      String familyName = user.getAttribute("family_name");
      String picture = user.getAttribute("picture");
      Boolean emailVerified = user.getAttribute("email_verified");

      // Current date and time for last login
      Date currentDate = new Date();
      LocalTime currentTime = LocalTime.now();

      // Check if user already exists in the database
      Optional<Login> existingUser = loginRepository.findById(email);

      if (existingUser.isPresent()) {
        // User exists, update last login time
        Login login = existingUser.get();
        login.setLastLogin(currentDate);
        login.setLastLoginTime(currentTime);
        loginRepository.save(login);
      } else {
        // User doesn't exist, create new entry
        Login newUser = new Login();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setGivenName(givenName);
        newUser.setFamilyName(familyName);
        newUser.setPicture(picture);
        newUser.setEmailVerified(emailVerified);
        newUser.setLastLogin(currentDate);
        newUser.setLastLoginTime(currentTime);

        // Save the new user to the database
        loginRepository.save(newUser);
      }
    }
    return "index"; // Render the index.html template
  }
}
