package com.ahmedsameha1.todo.domain_model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Size(min = 5, max = 100)
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDate birthDay;

    @NotNull
    @Column(nullable = false)
    private Gender gender = Gender.UNSPECIFIED;

    @NotNull
    @OneToMany(mappedBy = "user")
    private List<Todo> todos = Collections.emptyList();

    @NotNull
    @Column(nullable = false)
    private boolean enabled = false;

    @NotNull
    @Column(nullable = false)
    private boolean locked = false;

    @NotNull
    @Column(nullable = false)
    private boolean expired = false;

    @NotNull
    @Column(nullable = false)
    private boolean credentialsExpired = false;

    @Version
    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    @Column(nullable = false)
    private long version = 0L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
