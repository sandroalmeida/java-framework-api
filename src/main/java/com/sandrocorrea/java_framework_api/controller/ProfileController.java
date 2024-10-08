package com.sandrocorrea.java_framework_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/add-profile")
    public String showAddProfilePage() {
        return "add-profile";
    }

    @GetMapping("view-profiles")
    public String showViewProfilesPage() {
        return "view-profiles";
    }
}
