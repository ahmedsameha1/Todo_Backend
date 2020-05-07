package com.ahmedsameha1.todo.repository;

import com.ahmedsameha1.todo.domain_model.PasswordResetToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUserAccount(UserAccount userAccount);
}
