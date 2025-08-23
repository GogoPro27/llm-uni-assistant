package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
