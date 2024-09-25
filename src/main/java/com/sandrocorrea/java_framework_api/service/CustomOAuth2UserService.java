package com.sandrocorrea.java_framework_api.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // Extract the information you need from the OAuth2User and map it to a custom object
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        // Map attributes to your custom user entity or service logic
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(oAuth2User.getAttributes())),
                oAuth2User.getAttributes(),
                "name"); // Map "name" as the key attribute
    }
}