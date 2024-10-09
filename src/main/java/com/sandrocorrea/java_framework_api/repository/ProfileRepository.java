package com.sandrocorrea.java_framework_api.repository;

import com.sandrocorrea.java_framework_api.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {}
