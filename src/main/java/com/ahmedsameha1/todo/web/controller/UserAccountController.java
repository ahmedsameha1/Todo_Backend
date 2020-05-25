package com.ahmedsameha1.todo.web.controller;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.security.SignInRequest;
import com.ahmedsameha1.todo.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.ahmedsameha1.todo.Constants.*;

@RestController
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<UserAccount> signUp(@RequestBody @Valid UserAccount userAccount, HttpServletRequest request) {
        userAccount = userAccountService.registerUserAccount(userAccount, request);
        return ResponseEntity.created(URI.create("/user_account")).body(userAccount);
    }

    @GetMapping(EMAIL_VERIFICATION_URL)
    public ResponseEntity<?> emailVerification(@RequestParam("token") UUID token,
                                               HttpServletRequest request) {
        userAccountService.handleUserAccountEnablingProcess(token, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(SIGN_IN_URL)
    public ResponseEntity<Map<String, String>> signIn(@RequestBody @Valid SignInRequest signInRequest,
                                                      HttpServletRequest httpServletRequest) {
        var jws = userAccountService.authenticate(signInRequest, httpServletRequest);
        return ResponseEntity.ok(Map.of("jwt", jws));
    }
}
