package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.security.SignInRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserAccountService {
    void registerUserAccount(UserAccount userAccount, HttpServletRequest request)
            throws UserExistsException;
    EmailVerificationToken createEmailVerificationToken(UserAccount userAccount);
    void handleUserAccountEnablingProcess(String token, HttpServletRequest httpServletRequest);
    String authenticate(SignInRequest signInRequest, HttpServletRequest httpServletRequest);
}
