package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.model.Login;
import com.sandrocorrea.java_framework_api.repository.LoginRepository;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class HomeController {

  private final LoginRepository loginRepository;
  private final WebClient webClient;
  private final OAuth2AuthorizedClientService authorizedClientService;

  public HomeController(
      LoginRepository loginRepository,
      WebClient webClient,
      OAuth2AuthorizedClientService authorizedClientService) {
    this.loginRepository = loginRepository;
    this.webClient = webClient;
    this.authorizedClientService = authorizedClientService;
  }

  @GetMapping("/")
  public String index(Model model, OAuth2AuthenticationToken authentication) {
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
      } else if ("linkedin".equals(registrationId)) {
        // Handle LinkedIn login
        OAuth2AuthorizedClient client = getAuthorizedClient(authentication);
        Map<String, Object> linkedInData = fetchLinkedInUserData(client);

        email = (String) linkedInData.get("emailAddress");
        name =
            (String) linkedInData.get("localizedFirstName")
                + " "
                + (String) linkedInData.get("localizedLastName");
        picture = extractProfilePictureUrl(linkedInData);
        emailVerified = true;
      }

      // Display name on the page
      model.addAttribute("name", name);

      // Current date and time for last login
      Date currentDate = new Date();
      LocalTime currentTime = LocalTime.now();

      // Check if the user already exists in the database
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
    return "index"; // Render the index.html template
  }

  // Helper method to get OAuth2AuthorizedClient for LinkedIn API access
  private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    OAuth2AuthorizedClient client =
        authorizedClientService.loadAuthorizedClient(
            authentication.getAuthorizedClientRegistrationId(), currentAuth.getName());
    return client;
  }

  // Helper method to call LinkedIn API and fetch user data
  private Map<String, Object> fetchLinkedInUserData(OAuth2AuthorizedClient authorizedClient) {
    String userInfoEndpointUri =
        "https://api.linkedin.com/v2/me?projection=(id,localizedFirstName,localizedLastName,profilePicture(displayImage~:playableStreams))";
    return webClient
        .get()
        .uri(userInfoEndpointUri)
        .attributes(
            ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(
                authorizedClient))
        .retrieve()
        .bodyToMono(Map.class)
        .block();
  }

  // Helper method to extract LinkedIn profile picture URL
  private String extractProfilePictureUrl(Map<String, Object> linkedInData) {
    // LinkedIn's profile picture data structure is nested, so extract it accordingly
    Map<String, Object> profilePictureData =
        (Map<String, Object>) linkedInData.get("profilePicture");
    if (profilePictureData != null) {
      Map<String, Object> displayImage =
          (Map<String, Object>) profilePictureData.get("displayImage~");
      List<Map<String, Object>> elements = (List<Map<String, Object>>) displayImage.get("elements");
      if (!elements.isEmpty()) {
        Map<String, Object> firstElement = elements.get(0);
        List<Map<String, Object>> identifiers =
            (List<Map<String, Object>>) firstElement.get("identifiers");
        if (!identifiers.isEmpty()) {
          return (String) identifiers.get(0).get("identifier");
        }
      }
    }
    return null;
  }
}
