package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;

public interface UserAccountService {
    UserAccount registerNewUserAccount(UserAccount userAccount) throws UserExistsException;
    EmailVerificationToken createEmailVerificationToken(UserAccount userAccount);
}
