package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ahmedsameha1.todo.security.Constants.EMAIL_VERIFICATION_TOKEN_EXPIRATION_PERIOD_IN_DAYS;
import static com.ahmedsameha1.todo.security.Constants.JWT_TOKEN_EXPIRATION_PERIOD_IN_DAYS;

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

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Override
    public UserAccount registerNewUserAccount(UserAccount userAccount) throws UserExistsException {
        if (userAccountRepository.findByUsername(userAccount.getUsername()) != null) {
            throw new UserExistsException();
        }
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
        return userAccount;
    }

    @Override
    public EmailVerificationToken createEmailVerificationToken(UserAccount userAccount) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(UUID.randomUUID().toString());
        emailVerificationToken.setExpiresAt(LocalDateTime
                .now(Clock.systemUTC()).plusDays(EMAIL_VERIFICATION_TOKEN_EXPIRATION_PERIOD_IN_DAYS));
        emailVerificationToken.setUserAccount(userAccount);
        emailVerificationTokenRepository.save(emailVerificationToken);
        return emailVerificationToken;
    }

    @Override
    @Transactional
    public void enableUserAccount(String token) throws BadEmailVerificationTokenException {
        var emailVerificationToken = emailVerificationTokenRepository.findByToken(token);
        if (emailVerificationToken == null
                || emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now(Clock.systemUTC()))) {
            throw new BadEmailVerificationTokenException();
        }
        var userAccount = emailVerificationToken.getUserAccount();
        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);
        emailVerificationTokenRepository.delete(emailVerificationToken);
    }

    @Override
    public String authenticate(SignInRequest signInRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                            signInRequest.getPassword()));
        } catch (DisabledException de) {
           var userAccount = userAccountRepository.findByUsername(signInRequest.getUsername());
           var emailVerificationToken = emailVerificationTokenRepository.findByUserAccount(userAccount);
           if (emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now(Clock.systemUTC()))) {
               throw new ExpiredEmailVerificationTokenException();
           } else {
               throw de;
           }
        }
        return  Jwts.builder()
                .setSubject(((UserDetails) authentication.getPrincipal()).getUsername())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()).plusDays(JWT_TOKEN_EXPIRATION_PERIOD_IN_DAYS)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).compact();
    }
}
