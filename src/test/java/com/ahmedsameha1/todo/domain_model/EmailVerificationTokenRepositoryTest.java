package com.ahmedsameha1.todo.domain_model;

import com.ahmedsameha1.todo.repository.EmailVerificationTokenRepository;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmailVerificationTokenRepositoryTest extends ProductionDatabaseBaseTest {
    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private EmailVerificationToken emailVerificationToken;
    private UserAccount userAccount;

    @BeforeEach
    public void before() {
        userAccount = new UserAccount();
        userAccount.setUsername(UUID.randomUUID().toString());
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
        userAccount = userAccountRepository.save(userAccount);
        emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(UUID.randomUUID());
        emailVerificationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        emailVerificationToken.setUserAccount(userAccount);
    }

    @Test
    @DisplayName("Test saving EmailVerificationToken")
    public void test1() {
        emailVerificationTokenRepository.save(emailVerificationToken);
        var databaseEmailVerificationToken = emailVerificationTokenRepository.findByUserAccount(userAccount);
        assertThat(databaseEmailVerificationToken).isNotNull();
    }

    @Test
    @DisplayName("Test that the saved EmailVerificationToken has Id")
    public void test2() {
        emailVerificationTokenRepository.save(emailVerificationToken);
        var databaseEmailVerificationToken = emailVerificationTokenRepository.findByUserAccount(userAccount);
        assertThat(databaseEmailVerificationToken.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test saving EmailVerificationToken with the correct values")
    public void test3() {
        var token = UUID.randomUUID();
        var expiredAt = LocalDateTime.now().plusDays(1);
        emailVerificationToken.setToken(token);
        emailVerificationToken.setExpiresAt(expiredAt);
        emailVerificationToken = emailVerificationTokenRepository.save(emailVerificationToken);
        var databaseEmailVerificationToken = emailVerificationTokenRepository
                .findById(emailVerificationToken.getId()).get();
        assertThat(databaseEmailVerificationToken.getToken()).isEqualTo(token);
        assertThat(databaseEmailVerificationToken.getExpiresAt()).isEqualTo(expiredAt);
        assertThat(databaseEmailVerificationToken.getUserAccount()).isEqualTo(userAccount);
    }

    @Test
    @DisplayName("Should fail because token is null")
    public void test4() {
        emailVerificationToken.setToken(null);
        assertThatThrownBy(() -> emailVerificationTokenRepository.save(emailVerificationToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because there is an EmailVerificationToken with the same token")
    public void test5() {
        var token = UUID.randomUUID();
        emailVerificationToken.setToken(token);
        emailVerificationTokenRepository.save(emailVerificationToken);
        emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        userAccount = new UserAccount();
        userAccount.setUsername(UUID.randomUUID().toString());
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
        userAccount = userAccountRepository.save(userAccount);
        emailVerificationToken.setUserAccount(userAccount);
        assertThatThrownBy(() -> emailVerificationTokenRepository.save(emailVerificationToken))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(PSQLException.class);
    }

    @Test
    @DisplayName("Should fail because expiredAt is null")
    public void test6() {
        emailVerificationToken.setExpiresAt(null);
        assertThatThrownBy(() -> emailVerificationTokenRepository.save(emailVerificationToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because expiredAt isn't in the future")
    public void test7() {
        emailVerificationToken.setExpiresAt(LocalDateTime.now().minusDays(1));
        assertThatThrownBy(() -> emailVerificationTokenRepository.save(emailVerificationToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because UserAccount is null")
    public void test8() {
        emailVerificationToken.setUserAccount(null);
        assertThatThrownBy(() -> emailVerificationTokenRepository.save(emailVerificationToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because there is an EmailVerificationToken with the same UserAccount")
    public void test9() {
        userAccount = new UserAccount();
        userAccount.setUsername(UUID.randomUUID().toString());
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
        userAccount = userAccountRepository.save(userAccount);
        emailVerificationToken.setUserAccount(userAccount);
        emailVerificationTokenRepository.save(emailVerificationToken);
        emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(UUID.randomUUID());
        emailVerificationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        emailVerificationToken.setUserAccount(userAccount);
        assertThatThrownBy(() -> emailVerificationTokenRepository.save(emailVerificationToken))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(PSQLException.class);
    }
}
