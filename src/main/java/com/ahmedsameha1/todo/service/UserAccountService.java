package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.security.SignInRequest;

public interface UserAccountService {
    UserAccount registerNewUserAccount(UserAccount userAccount) throws UserExistsException;
    EmailVerificationToken createEmailVerificationToken(UserAccount userAccount);
    void enableUserAccount(String token);
    String authenticate(SignInRequest signInRequest);
}
