package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
  Optional<User> findByEmail(@Param("email") String email);
}
