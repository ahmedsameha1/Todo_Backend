package com.ahmedsameha1.todo.domain_model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class User extends BaseEntity {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDay;
    private Gender gender;
    @OneToMany(mappedBy = "user")
    private List<Todo> todos;
}
