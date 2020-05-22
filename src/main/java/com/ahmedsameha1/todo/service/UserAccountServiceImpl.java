package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.email_verification.NeedEmailVerificationToken;
import com.ahmedsameha1.todo.exception.BadEmailVerificationTokenException;
import com.ahmedsameha1.todo.exception.ExpiredEmailVerificationTokenException;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.repository.EmailVerificationTokenRepository;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import com.ahmedsameha1.todo.security.SignInRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ahmedsameha1.todo.Constants.EMAIL_VERIFICATION_TOKEN_EXPIRATION_PERIOD_IN_DAYS;
import static com.ahmedsameha1.todo.Constants.JWT_TOKEN_EXPIRATION_PERIOD_IN_DAYS;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Override
    public UserAccount registerUserAccount(UserAccount userAccount,
                                    HttpServletRequest request)
            throws UserExistsException {
        if (userAccountRepository.findByUsername(userAccount.getUsername()) != null) {
            throw new UserExistsException();
        }
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        userAccount = userAccountRepository.save(userAccount);
        var appUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
        applicationEventPublisher
                .publishEvent(new NeedEmailVerificationToken(userAccount, appUrl, request.getLocale()));
        return userAccount;
    }

    @Override
    public EmailVerificationToken createEmailVerificationToken(UserAccount userAccount) {
        var emailVerificationToken = emailVerificationTokenRepository.findByUserAccount(userAccount);
        if (emailVerificationToken == null) {
            emailVerificationToken = new EmailVerificationToken();
            emailVerificationToken.setUserAccount(userAccount);
        }
        emailVerificationToken.setToken(UUID.randomUUID());
        emailVerificationToken.setExpiresAt(LocalDateTime
                .now().plusDays(EMAIL_VERIFICATION_TOKEN_EXPIRATION_PERIOD_IN_DAYS));
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public void handleUserAccountEnablingProcess(String token, HttpServletRequest httpServletRequest) {
        var emailVerificationToken = emailVerificationTokenRepository.findByToken(token);
        if (validateEmailVerificationToken(emailVerificationToken, httpServletRequest)) {
            enableUserAccount(emailVerificationToken);
        }
    }

    private boolean validateEmailVerificationToken(EmailVerificationToken emailVerificationToken,
                                                   HttpServletRequest httpServletRequest) {
        if (emailVerificationToken == null) {
            throw new BadEmailVerificationTokenException();
        }
        if (emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            var appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName()
                    + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
            applicationEventPublisher
                    .publishEvent(new NeedEmailVerificationToken(emailVerificationToken.getUserAccount(),
                            appUrl, httpServletRequest.getLocale()));
            throw new ExpiredEmailVerificationTokenException();
        }
        return true;
    }

    @Transactional
    private void enableUserAccount(EmailVerificationToken emailVerificationToken) {
        var userAccount = emailVerificationToken.getUserAccount();
        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);
        emailVerificationTokenRepository.delete(emailVerificationToken);
    }

    @Override
    public String authenticate(SignInRequest signInRequest, HttpServletRequest httpServletRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                            signInRequest.getPassword()));
        } catch (DisabledException de) {
           var userAccount = userAccountRepository.findByUsername(signInRequest.getUsername());
           var emailVerificationToken = emailVerificationTokenRepository.findByUserAccount(userAccount);
           if (emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
               var appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName()
                       + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
               applicationEventPublisher
                       .publishEvent(new NeedEmailVerificationToken(userAccount, appUrl, httpServletRequest.getLocale()));
               throw new ExpiredEmailVerificationTokenException();
           } else {
               throw de;
           }
        }
        return  Jwts.builder()
                .setSubject(((UserDetails) authentication.getPrincipal()).getUsername())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusDays(JWT_TOKEN_EXPIRATION_PERIOD_IN_DAYS)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).compact();
    }
}
