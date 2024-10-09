package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.model.Profile;
import com.sandrocorrea.java_framework_api.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

  private final ProfileService profileService;

  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/add-profile")
  public String showAddProfilePage(Model model) {
    model.addAttribute("profile", new Profile());
    return "add-profile";
  }

  @PostMapping("/save-profile")
  public String saveProfile(@ModelAttribute Profile profile, Model model, BindingResult result) {
    if (result.hasErrors()) {
      model.addAttribute("message", "Please correct the errors in the form");
      return "profile :: content";
    }
    profileService.saveProfile(profile);
    model.addAttribute("message", "Profile saved successfully!");
    return "view-profiles";
  }

  @GetMapping("view-profiles")
  public String showViewProfilesPage() {
    return "view-profiles";
  }
}
