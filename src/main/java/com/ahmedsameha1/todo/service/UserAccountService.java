package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import org.springframework.stereotype.Service;

@Service
public interface UserAccountService {
    UserAccount registerNewUserAccount(UserAccount userAccount) throws UserExistsException;
}
