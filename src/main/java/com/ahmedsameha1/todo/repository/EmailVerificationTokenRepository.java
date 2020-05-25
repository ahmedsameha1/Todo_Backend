package com.ahmedsameha1.todo.repository;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    EmailVerificationToken findByToken(UUID token);
    EmailVerificationToken findByUserAccount(UserAccount userAccount);
}
