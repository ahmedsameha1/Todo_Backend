package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserAccount registerNewUserAccount(UserAccount userAccount) throws UserExistsException {
        if (userAccountRepository.findByUsername(userAccount.getUsername()) != null) {
            throw new UserExistsException();
        }
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
        return userAccount;
    }
}
