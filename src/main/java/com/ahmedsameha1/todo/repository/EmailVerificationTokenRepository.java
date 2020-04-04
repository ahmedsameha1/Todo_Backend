package com.ahmedsameha1.todo.repository;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    EmailVerificationToken findByToken(String token);
}
