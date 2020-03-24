package com.ahmedsameha1.todo.domain_model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Todo extends BaseEntity {
    private String description;
    private LocalDate targetDate;
    private boolean isDone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
