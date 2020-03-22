package com.ahmedsameha1.todo.domain_model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
public class Todo extends BaseEntity {
    private String description;
    private LocalDate targetDate;
    private boolean isDone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
