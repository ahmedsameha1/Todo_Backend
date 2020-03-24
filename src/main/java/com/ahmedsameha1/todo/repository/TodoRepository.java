package com.ahmedsameha1.todo.repository;

import com.ahmedsameha1.todo.domain_model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
}
