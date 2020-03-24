package com.ahmedsameha1.todo.repository;

import com.ahmedsameha1.todo.domain_model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}
