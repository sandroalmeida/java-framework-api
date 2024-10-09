package com.sandrocorrea.java_framework_api.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "profile", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 50, nullable = false)
  private String role;

  @Column(nullable = false)
  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate date;

  @Column(nullable = false)
  private Boolean status = true;

  @Column(length = 1000)
  private String description;
}
