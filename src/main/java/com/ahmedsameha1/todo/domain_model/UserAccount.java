package com.ahmedsameha1.todo.domain_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccount extends BaseEntity implements UserDetails {
    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^\\S+$")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$")
    @Column(nullable = false)
    @Getter(onMethod_ = {@JsonIgnore})
    @Setter(onMethod_ = {@JsonProperty})
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
    @Size(min = 5, max = 255)
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDate birthDay;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNSPECIFIED;

    @NotNull
    @JsonIgnore
    @OneToMany(mappedBy = "userAccount")
    private List<Todo> todos = new ArrayList<>();

    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private boolean enabled = false;

    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private boolean locked = false;

    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private boolean expired = false;

    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private boolean credentialsExpired = false;

    @Version
    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    @JsonIgnore
    @Column(nullable = false)
    private long version = 0L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
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
