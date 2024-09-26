package com.sandrocorrea.java_framework_api.repository;

import com.sandrocorrea.java_framework_api.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, String> {}
