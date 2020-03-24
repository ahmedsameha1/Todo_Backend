package com.ahmedsameha1.todo.domain_model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDay;
    private Gender gender;
    @OneToMany(mappedBy = "user")
    private List<Todo> todos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
