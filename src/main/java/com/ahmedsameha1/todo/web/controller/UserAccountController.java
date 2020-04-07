package com.ahmedsameha1.todo.web.controller;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.ahmedsameha1.todo.security.Constants.SIGN_UP_URL;

@RestController
public class UserAccountController {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<?> signUp(@RequestBody @Valid UserAccount userAccount) {
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
        return ResponseEntity.ok().build();
    }
}
