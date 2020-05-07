package com.ahmedsameha1.todo.domain_model;

import com.ahmedsameha1.todo.repository.PasswordResetTokenRepository;
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

public class PasswordResetTokenRepositoryTest extends ProductionDatabaseBaseTest {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private PasswordResetToken passwordResetToken;
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
        passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID());
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        passwordResetToken.setUserAccount(userAccount);
    }

    @Test
    @DisplayName("Test saving PasswordRestToken")
    public void test1() {
        passwordResetTokenRepository.save(passwordResetToken);
        var databasePasswordRestToken = passwordResetTokenRepository.findByUserAccount(userAccount);
        assertThat(databasePasswordRestToken).isNotNull();
    }

    @Test
    @DisplayName("Test that the saved PasswordRestToken has Id")
    public void test2() {
        passwordResetTokenRepository.save(passwordResetToken);
        var databasePasswordRestToken = passwordResetTokenRepository.findByUserAccount(userAccount);
        assertThat(databasePasswordRestToken.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test saving PasswordRestToken with the correct values")
    public void test3() {
        var token = UUID.randomUUID();
        var expiresAt = LocalDateTime.now().plusDays(1);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiresAt(expiresAt);
        passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
        var databasePasswordRestToken = passwordResetTokenRepository
                .findById(passwordResetToken.getId()).get();
        assertThat(databasePasswordRestToken.getToken()).isEqualTo(token);
        assertThat(databasePasswordRestToken.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(databasePasswordRestToken.getUserAccount()).isEqualTo(userAccount);
    }

    @Test
    @DisplayName("Should fail because token is null")
    public void test4() {
        passwordResetToken.setToken(null);
        assertThatThrownBy(() -> passwordResetTokenRepository.save(passwordResetToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because there is an PasswordRestToken with the same token")
    public void test5() {
        var token = UUID.randomUUID();
        passwordResetToken.setToken(token);
        passwordResetTokenRepository.save(passwordResetToken);
        passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        userAccount = new UserAccount();
        userAccount.setUsername(UUID.randomUUID().toString());
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
        userAccount = userAccountRepository.save(userAccount);
        passwordResetToken.setUserAccount(userAccount);
        assertThatThrownBy(() -> passwordResetTokenRepository.save(passwordResetToken))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(PSQLException.class);
    }

    @Test
    @DisplayName("Should fail because expiresAt is null")
    public void test6() {
        passwordResetToken.setExpiresAt(null);
        assertThatThrownBy(() -> passwordResetTokenRepository.save(passwordResetToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because expiresAt isn't in the future")
    public void test7() {
        passwordResetToken.setExpiresAt(LocalDateTime.now().minusDays(1));
        assertThatThrownBy(() -> passwordResetTokenRepository.save(passwordResetToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because UserAccount is null")
    public void test8() {
        passwordResetToken.setUserAccount(null);
        assertThatThrownBy(() -> passwordResetTokenRepository.save(passwordResetToken))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because there is an PasswordRestToken with the same UserAccount")
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
        passwordResetToken.setUserAccount(userAccount);
        passwordResetTokenRepository.save(passwordResetToken);
        passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID());
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        passwordResetToken.setUserAccount(userAccount);
        assertThatThrownBy(() -> passwordResetTokenRepository.save(passwordResetToken))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(PSQLException.class);
    }
}
