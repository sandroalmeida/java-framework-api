package com.sandrocorrea.java_framework_api.service;

import com.sandrocorrea.java_framework_api.model.Profile;
import com.sandrocorrea.java_framework_api.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

  private final ProfileRepository profileRepository;

  public ProfileService(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }
  public Profile saveProfile(Profile profile) {
    return profileRepository.save(profile);
  }

  public Page<Profile> getAllProfiles(Pageable pageable) {
    return profileRepository.findAll(pageable);
  }
}
