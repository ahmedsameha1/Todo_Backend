package com.ahmedsameha1.todo.domain_model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class EmailVerificationToken extends BaseEntity {
    @NotBlank
    @Column(nullable = false)
    private String token;

    @Future
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private UserAccount userAccount;
}
