package com.ahmedsameha1.todo.domain_model;

import com.ahmedsameha1.todo.repository.TodoRepository;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TodoRepositoryTest extends ProductionDatabaseBaseTest {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private Todo todo;
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
        todo = new Todo();
        todo.setDescription("Pay internet service subscription");
        todo.setTargetDate(LocalDate.of(2025, 10, 10));
        todo.setUserAccount(userAccount);
    }

    @Nested
    @DisplayName("Todo object saving tests")
    class TodoObject {
        @Test
        @DisplayName("Test saving todo")
        public void test1() {
            todo = todoRepository.save(todo);
            Todo databaseTodo = todoRepository.findById(todo.getId()).get();
            assertThat(databaseTodo).isNotNull();
        }

        @Test
        @DisplayName("Test saving todo with the correct values")
        public void test2() {
            todo.setDone(true);
            todo = todoRepository.save(todo);
            Todo databaseTodo = todoRepository.findById(todo.getId()).get();
            assertThat(databaseTodo.getDescription()).isEqualTo("Pay internet service subscription");
            assertThat(databaseTodo.getTargetDate()).isEqualTo(LocalDate.of(2025, 10, 10));
            assertThat(databaseTodo.getUserAccount()).isEqualTo(userAccount);
            assertThat(databaseTodo.isDone()).isTrue();
        }
    }

    @Nested
    @DisplayName("Description tests")
    class Description {
        @Test
        @DisplayName("Should fail because description is null")
        public void test1() {
            todo.setDescription(null);
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because description is an empty string")
        public void test2() {
            todo.setDescription("");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because description has only whitespace")
        public void test3() {
            todo.setDescription(" ");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            todo.setDescription("\t");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            todo.setDescription("\n");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            todo.setDescription("\r");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            todo.setDescription("\t\n");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
            todo.setDescription("\r ");
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should fail because the length of description is larger than 200000000")
        public void test4() {
            todo.setDescription("f".repeat(200000001));
            assertThatThrownBy(() -> todoRepository.save(todo))
                    .hasRootCauseInstanceOf(ConstraintViolationException.class);
        }
    }
}
