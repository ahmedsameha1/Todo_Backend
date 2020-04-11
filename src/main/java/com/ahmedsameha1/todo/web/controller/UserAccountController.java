package com.ahmedsameha1.todo.web.controller;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.email_verification.OnRegistrationCompleteEvent;
import com.ahmedsameha1.todo.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ahmedsameha1.todo.security.Constants.EMAIL_VERIFICATION_URL;
import static com.ahmedsameha1.todo.security.Constants.SIGN_UP_URL;

@RestController
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<?> signUp(@RequestBody @Valid UserAccount userAccount, HttpServletRequest request) {
        userAccount = userAccountService.registerNewUserAccount(userAccount);
        applicationEventPublisher
                .publishEvent(new OnRegistrationCompleteEvent(userAccount, request.getContextPath()));
        return ResponseEntity.ok().build();
    }

    @GetMapping(EMAIL_VERIFICATION_URL)
    public ResponseEntity<?> emailVerification(@RequestParam("token") String token) {
        return userAccountService.enableUserAccount(token)?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
