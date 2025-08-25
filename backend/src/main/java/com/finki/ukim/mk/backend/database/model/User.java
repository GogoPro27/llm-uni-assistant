package com.finki.ukim.mk.backend.database.model;

import com.finki.ukim.mk.backend.database.model.enums.UserRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @ElementCollection
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role", nullable = false, columnDefinition = "user_role")
  @Enumerated(EnumType.STRING)
  @Default
  private Set<UserRole> roles = new HashSet<>();

  @OneToOne(mappedBy = "user")
  private Student student;

  @OneToOne(mappedBy = "user")
  private Professor professor;

  public boolean isStudent() {
    return student != null;
  }

  public boolean isProfessor() {
    return professor != null;
  }

  public boolean hasRole() {
    return student != null || professor != null;
  }
}
