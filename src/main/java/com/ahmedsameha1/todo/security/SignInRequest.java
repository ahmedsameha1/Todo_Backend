package com.ahmedsameha1.todo.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter @Getter
public class SignInRequest {
    @NotBlank(message = "username must be present")
    private String username;

    @NotBlank
    private String password;
}
