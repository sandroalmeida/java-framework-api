package com.sandrocorrea.java_framework_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "login", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Id
    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 500, nullable = false)
    private String name;

    @Column(length = 500)
    private String givenName;

    @Column(length = 500)
    private String familyName;

    @Column(length = 1000)
    private String picture;

    @Column
    private Boolean emailVerified;

    @Column
    private Date lastLogin;

    @Column
    private LocalTime lastLoginTime;
}
