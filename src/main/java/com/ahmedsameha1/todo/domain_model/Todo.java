package com.ahmedsameha1.todo.domain_model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Todo extends BaseEntity {
    @NotBlank
    @Size(min = 1, max = 200000000)
    @Column(nullable = false)
    private String description;

    @Future
    @Column(nullable = false)
    private LocalDate targetDate;

    @NotNull
    @Column(nullable = false)
    private boolean isDone = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
