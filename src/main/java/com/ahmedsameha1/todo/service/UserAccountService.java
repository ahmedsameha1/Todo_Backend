package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.security.SignInRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface UserAccountService {
    UserAccount registerUserAccount(UserAccount userAccount, HttpServletRequest request)
            throws UserExistsException;
    EmailVerificationToken createEmailVerificationToken(UserAccount userAccount);
    void handleUserAccountEnablingProcess(UUID token, HttpServletRequest httpServletRequest);
    String authenticate(SignInRequest signInRequest, HttpServletRequest httpServletRequest);
}
