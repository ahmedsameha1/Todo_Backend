package com.ahmedsameha1.todo.domain_model;

import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserAccountRepositoryTest extends ProductionDatabaseBaseTest {
    @Autowired
    private UserAccountRepository userAccountRepository;

    private UserAccount userAccount;

    // Because saving user account with the same username isn't allowed
    private String randomUserName;

    @BeforeEach
    public void beforeEach() {
        randomUserName = UUID.randomUUID().toString();
        userAccount = new UserAccount();
        userAccount.setUsername(randomUserName);
        userAccount.setPassword("ffffffff");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
    }

    @Test
    @DisplayName("Test saving user account")
    public void test1() {
        userAccountRepository.save(userAccount);
        var databaseUserAccount = userAccountRepository.findByUsername(randomUserName);
        assertThat(databaseUserAccount).isNotNull();
    }

    @Test
    @DisplayName("Test that the saved user account has a id")
    public void test2() {
        userAccountRepository.save(userAccount);
        var databaseUserAccount = userAccountRepository.findByUsername(randomUserName);
        assertThat(databaseUserAccount.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test saving user account with the correct values")
    public void test3() {
        userAccountRepository.save(userAccount);
        var databaseUserAccount = userAccountRepository.findByUsername(randomUserName);
        assertThat(databaseUserAccount.getUsername()).isEqualTo(randomUserName);
        assertThat(databaseUserAccount.getPassword()).isEqualTo("ffffffff");
        assertThat(databaseUserAccount.getFirstName()).isEqualTo("user2");
        assertThat(databaseUserAccount.getLastName()).isEqualTo("user2");
        assertThat(databaseUserAccount.getGender()).isEqualTo(Gender.MALE);
        assertThat(databaseUserAccount.getBirthDay())
                .isEqualTo(LocalDate.of(2010, 10, 10));
        assertThat(databaseUserAccount.getEmail()).isEqualTo("user2@user2.com");
    }

    @Test
    @DisplayName("Should fail because username is null")
    public void test4() {
        userAccount.setUsername(null);
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because username is an empty string")
    public void test5() {
        userAccount.setUsername("");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because username is a string that only contains whitespace")
    public void test6() {
        userAccount.setUsername("  ");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
        userAccount.setUsername("\n");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
        userAccount.setUsername("\r");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
        userAccount.setUsername("\t");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because username length is more than 50 character")
    public void test7() {
        userAccount.setUsername("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(TransactionSystemException.class)
                .hasCauseInstanceOf(RollbackException.class)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because username has whitespace")
    public void test8() {
        userAccount.setUsername("t u");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
        userAccount.setUsername(" tu");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
        userAccount.setUsername("tu ");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Should fail because there is a user account with the same username")
    public void test9() {
        userAccountRepository.save(userAccount);
        userAccount = new UserAccount();
        userAccount.setUsername(randomUserName);
        userAccount.setPassword("ffffffff");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
        assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(PSQLException.class);
    }
}
