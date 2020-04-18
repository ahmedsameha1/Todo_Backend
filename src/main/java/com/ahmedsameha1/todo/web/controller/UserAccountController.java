package com.ahmedsameha1.todo.web.controller;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.email_verification.OnRegistrationCompleteEvent;
import com.ahmedsameha1.todo.security.SignInRequest;
import com.ahmedsameha1.todo.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static com.ahmedsameha1.todo.Constants.*;

@RestController
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MessageSource messageSource;

    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<?> signUp(@RequestBody @Valid UserAccount userAccount, HttpServletRequest request) {
        userAccount = userAccountService.registerNewUserAccount(userAccount);
        var appUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
        applicationEventPublisher
                .publishEvent(new OnRegistrationCompleteEvent(userAccount, appUrl, request.getLocale()));
        String message = messageSource.getMessage("signUpSuccessfullyMessage",
                null, request.getLocale());
        return ResponseEntity.ok(message);
    }

    @GetMapping(EMAIL_VERIFICATION_URL)
    public ResponseEntity<?> emailVerification(@RequestParam("token") String token) {
        userAccountService.enableUserAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping(SIGN_IN_URL)
    public ResponseEntity<Map<String, String>> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        var jws = userAccountService.authenticate(signInRequest);
        return ResponseEntity.ok(Map.of("jwt", jws));
    }
}
