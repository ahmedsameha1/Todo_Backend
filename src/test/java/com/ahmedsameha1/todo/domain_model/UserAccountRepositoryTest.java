package com.ahmedsameha1.todo.domain_model;

import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.UUID;


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
        Assertions.assertThat(databaseUserAccount).isNotNull();
    }

    @Test
    @DisplayName("Test that the saved user account has a id")
    public void test2() {
        userAccountRepository.save(userAccount);
        var databaseUserAccount = userAccountRepository.findByUsername(randomUserName);
        Assertions.assertThat(databaseUserAccount.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test saving user account with the correct values")
    public void test3() {
        userAccountRepository.save(userAccount);
        var databaseUserAccount = userAccountRepository.findByUsername(randomUserName);
        Assertions.assertThat(databaseUserAccount.getUsername()).isEqualTo(randomUserName);
        Assertions.assertThat(databaseUserAccount.getPassword()).isEqualTo("ffffffff");
        Assertions.assertThat(databaseUserAccount.getFirstName()).isEqualTo("user2");
        Assertions.assertThat(databaseUserAccount.getLastName()).isEqualTo("user2");
        Assertions.assertThat(databaseUserAccount.getGender()).isEqualTo(Gender.MALE);
        Assertions.assertThat(databaseUserAccount.getBirthDay())
                .isEqualTo(LocalDate.of(2010, 10, 10));
        Assertions.assertThat(databaseUserAccount.getEmail()).isEqualTo("user2@user2.com");
    }
}
