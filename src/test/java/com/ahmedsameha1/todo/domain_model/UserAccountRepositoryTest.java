package com.ahmedsameha1.todo.domain_model;

import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

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
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
    }

    @Nested
    @DisplayName("User account object saving tests")
    class UserAccountObject {
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
            assertThat(databaseUserAccount.getPassword()).isEqualTo("ffffff3Q");
            assertThat(databaseUserAccount.getFirstName()).isEqualTo("user2");
            assertThat(databaseUserAccount.getLastName()).isEqualTo("user2");
            assertThat(databaseUserAccount.getGender()).isEqualTo(Gender.MALE);
            assertThat(databaseUserAccount.getBirthDay())
                    .isEqualTo(LocalDate.of(2010, 10, 10));
            assertThat(databaseUserAccount.getEmail()).isEqualTo("user2@user2.com");
        }
    }

    @Nested
    @DisplayName("username tests")
    class Username {
        @Test
        @DisplayName("Should fail because username is null")
        public void test1() {
            userAccount.setUsername(null);
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because username is an empty string")
        public void test2() {
            userAccount.setUsername("");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because username is a string that only contains whitespace")
        public void test3() {
            userAccount.setUsername("  ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setUsername("\n");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setUsername("\r");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setUsername("\t");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because username length is more than 50 character")
        public void test4() {
            userAccount.setUsername("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because username has whitespace")
        public void test5() {
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
        public void test6() {
            userAccountRepository.save(userAccount);
            userAccount = new UserAccount();
            userAccount.setUsername(randomUserName);
            userAccount.setPassword("ffffff3Q");
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

    @Nested
    @DisplayName("Password tests")
    class Password {
        @Test
        @DisplayName("Should fail because password is null")
        public void test1() {
            userAccount.setPassword(null);
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because password is a string that only contains whitespace")
        public void test2() {
            userAccount.setPassword("                           ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because password length is less than 8 characters")
        public void test3() {
            userAccount.setPassword("fffffff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because password length is more than 255 character")
        public void test4() {
            userAccount.setPassword("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
            + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
            + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
            + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
            + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because password has whitespace")
        public void test5() {
            userAccount.setPassword("tuaaaaaa aaaaaaaaaaaa");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword(" tupppppppppppppppppppp");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("qqqqqqqqqqqqqqqqqqqqqqqtu ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because password must have at least one lowercase character"
                + "and at least one uppercase character"
                + "and at least one digit")
        public void test6() {
            userAccount.setPassword("qqqqqqqqqqqq");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("QQQQQQQQQQQQ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("333333333333");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("qqqqqqqqqqqqQ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("qqqqqqqqqqqq3");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("33333333333q");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("33333333333Q");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("QQQQQQQQQQQq");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setPassword("QQQQQQQQQQQ3");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Passwords are correct because it have: "
        + "at least one lowercase character and "
        + "at least one uppercase character and "
        + "at least one digit and "
                + "at least 8 characters")
        public void test7() {
            userAccount.setPassword("576Agyrtbn");
            assertThatCode(() -> userAccountRepository.save(userAccount)).doesNotThrowAnyException();

        }
    }

    @Nested
    @DisplayName("FirstName tests")
    class FirstName {
        @Test
        @DisplayName("Should fail because firstName is null")
        public void test1() {
            userAccount.setFirstName(null);
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because firstName is an empty string")
        public void test2() {
            userAccount.setFirstName("");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because firstName is a string that only contains whitespace")
        public void test3() {
            userAccount.setFirstName("  ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\n");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\r");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\t");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because firstName length is more than 100 character")
        public void test4() {
            userAccount.setFirstName("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                    "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because firstName has whitespace either at the start or at the end")
        public void test5() {
            userAccount.setFirstName(" fff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("fff ");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\tfff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("fff\t");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\nfff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("fff\n");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\rfff");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("fff\r");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName("\nfff\r");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            userAccount.setFirstName(" fff\t");
            assertThatThrownBy(() -> userAccountRepository.save(userAccount))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }
    }
}
