package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.repository.EmailVerificationTokenRepository;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ahmedsameha1.todo.security.Constants.EMAIL_VERIFICATION_TOKEN_EXPIRATION_PERIOD_IN_DAYS;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;
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
    public boolean enableUserAccount(String token) {
        var emailVerificationToken = emailVerificationTokenRepository.findByToken(token);
        if (emailVerificationToken == null
                || emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now(Clock.systemUTC()))) {
            return false;
        }
        var userAccount = emailVerificationToken.getUserAccount();
        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);
        emailVerificationTokenRepository.delete(emailVerificationToken);
        return true;
    }
}
