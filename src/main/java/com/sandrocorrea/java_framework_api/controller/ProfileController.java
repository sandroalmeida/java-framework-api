package com.sandrocorrea.java_framework_api.controller;

import com.sandrocorrea.java_framework_api.model.Profile;
import com.sandrocorrea.java_framework_api.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String saveProfile(@ModelAttribute Profile profile, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("message", "Please correct the errors in the form");
      return "profile :: content";
    }
    profileService.saveProfile(profile);

    // Prepare the model for the view-profiles page
    int pageSize = 10; // Number of profiles per page
    Pageable pageable = PageRequest.of(0, pageSize, Sort.by("name").ascending());
    Page<Profile> profilePage = profileService.getAllProfiles(pageable);

    model.addAttribute("profilePage", profilePage);
    model.addAttribute("currentPage", 1);
    model.addAttribute("totalPages", profilePage.getTotalPages());
    model.addAttribute("totalItems", profilePage.getTotalElements());

    return "view-profiles :: profilesTable";
  }

  @GetMapping("/profiles")
  public String viewProfiles(@RequestParam(defaultValue = "1") int page, Model model) {
    int pageSize = 10; // Number of profiles per page
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("name").ascending());
    Page<Profile> profilePage = profileService.getAllProfiles(pageable);

    model.addAttribute("profilePage", profilePage);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", profilePage.getTotalPages());
    model.addAttribute("totalItems", profilePage.getTotalElements());

    return "view-profiles :: profilesTable";
  }
}
