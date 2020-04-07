package com.ahmedsameha1.todo.repository;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    UserAccount findByUsername(String username);
}
