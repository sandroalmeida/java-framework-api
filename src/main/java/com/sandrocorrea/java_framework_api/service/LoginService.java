package com.sandrocorrea.java_framework_api.service;

import com.sandrocorrea.java_framework_api.model.Login;
import com.sandrocorrea.java_framework_api.repository.LoginRepository;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class LoginService {
  private final LoginRepository loginRepository;

  public LoginService(LoginRepository loginRepository) {
    this.loginRepository = loginRepository;
  }

  public void processLogin(OAuth2AuthenticationToken authentication, Model model) {
    if (authentication != null) {
      // Get the OAuth2 user
      OAuth2User user = authentication.getPrincipal();
      // Identify the provider
      String registrationId = authentication.getAuthorizedClientRegistrationId();

      // Extract common attributes
      String email = null;
      String name = null;
      String givenName = null;
      String familyName = null;
      String picture = null;
      Boolean emailVerified = null;

      if ("google".equals(registrationId)) {
        // Handle Google login (OIDC attributes)
        email = user.getAttribute("email");
        name = user.getAttribute("name");
        givenName = user.getAttribute("given_name");
        familyName = user.getAttribute("family_name");
        picture = user.getAttribute("picture");
        emailVerified = user.getAttribute("email_verified");
      } else if ("github".equals(registrationId)) {
        // Handle GitHub login (GitHub attributes)
        email = user.getAttribute("email");
        name = user.getAttribute("login");
        picture = user.getAttribute("avatar_url");
        emailVerified = true;
      }

      // Display name on the page
      model.addAttribute("name", name);

      // Current date and time for last login
      Date currentDate = new Date();
      LocalTime currentTime = LocalTime.now();

      if (email != null) {
        Optional<Login> existingUser = loginRepository.findById(email);

        if (existingUser.isPresent()) {
          // User exists, update last login time
          Login login = existingUser.get();
          login.setLastLogin(currentDate);
          login.setLastLoginTime(currentTime);
          loginRepository.save(login);
        } else {
          // User doesn't exist, create a new entry
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
    }
  }
}
