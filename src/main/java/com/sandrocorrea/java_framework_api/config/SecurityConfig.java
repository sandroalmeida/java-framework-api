package com.sandrocorrea.java_framework_api.config;

import com.sandrocorrea.java_framework_api.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers(
                        "/landing",
                        "/login-modal",
                        "/oauth2/**",
                        "/static/favicon.ico",
                        "/css/**",
                        "/js/**",
                        "/images/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth2Login ->
                oauth2Login
                    .loginPage("/landing")
                    .defaultSuccessUrl("/", true)
                    .userInfoEndpoint(
                        userInfo -> {
                          userInfo.oidcUserService(this.oidcUserService());
                          userInfo.userService(this.oAuth2UserService());
                        }))
        .logout(logout -> logout.logoutSuccessUrl("/landing").permitAll());

    return http.build();
  }

  // Google OIDC user processing
  @Bean
  public OidcUserService oidcUserService() {
    return new OidcUserService();
  }

  // GitHub OAuth2 user processing
  @Bean
  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
    return new CustomOAuth2UserService();
  }
}
