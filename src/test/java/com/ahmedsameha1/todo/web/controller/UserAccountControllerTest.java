package com.ahmedsameha1.todo.web.controller;

import com.ahmedsameha1.todo.domain_model.Gender;
import com.ahmedsameha1.todo.domain_model.ProductionDatabaseBaseTest;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.service.UserAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import static com.ahmedsameha1.todo.Constants.ErrorCode.USER_EXISTS;
import static com.ahmedsameha1.todo.Constants.ErrorCode.VALIDATION;
import static com.ahmedsameha1.todo.Constants.SIGN_UP_URL;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UserAccountControllerTest extends ProductionDatabaseBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountService userAccountService;

    @MockBean
    private MessageSource messageSource;

    private UserAccount userAccount;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String message = "message";

    @BeforeEach
    public void beforeEach() {
        userAccount = new UserAccount();
        userAccount.setUsername(UUID.randomUUID().toString());
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
    }

    @Nested
    @DisplayName("SignUp Tests")
    class SignUp {
        @Test
        @DisplayName("Should pass because the sent request body is a json that represents a valid UserAccount")
        public void test1() throws Exception {
            when(messageSource.getMessage(eq("signUpSuccessfullyMessage"), isNull(), eq(Locale.getDefault()))).thenReturn(message);
            mockMvc.perform(post(SIGN_UP_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonedUserAccount()).locale(Locale.getDefault()))
                    .andExpect(status().isOk())
                    .andExpect(content().string(message));
        }

        @Nested
        class Username {
            @Test
            @DisplayName("Should fail because the sent request body is a json that doesn't have a username")
            public void test1() throws Exception {
                userAccount.setUsername(null);
                callEndPoint();
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has an empty username")
            public void test2() throws Exception {
                userAccount.setUsername("");
                callEndPoint();
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that contains only whitespace")
            public void test3() throws Exception {
                userAccount.setUsername("  ");
                callEndPoint();
                userAccount.setUsername("\n");
                callEndPoint();
                userAccount.setUsername("\r");
                callEndPoint();
                userAccount.setUsername("\t");
                callEndPoint();
                userAccount.setUsername("\t\n");
                callEndPoint();
                userAccount.setUsername("\t\r");
                callEndPoint();
                userAccount.setUsername("\r\n");
                callEndPoint();
                userAccount.setUsername("\r ");
                callEndPoint();
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that has more than 50 characters")
            public void test4() throws Exception {
                userAccount.setUsername("f".repeat(51));
                callEndPoint();
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that has whitespace")
            public void test5() throws Exception {
                userAccount.setUsername("t u");
                callEndPoint();
                userAccount.setUsername(" tu");
                callEndPoint();
                userAccount.setUsername("tu ");
                callEndPoint();
                userAccount.setUsername("tu\t");
                callEndPoint();
                userAccount.setUsername("\ntu ");
                callEndPoint();
                userAccount.setUsername("\tt u\r");
                callEndPoint();
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that is already exists")
            public void test6() throws Exception {
                doThrow(UserExistsException.class).when(userAccountService)
                        .registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
                when(messageSource.getMessage(eq("error.userExistsProblem"), isNull(), any(Locale.class))).thenReturn(message);
                mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonedUserAccount()).locale(Locale.getDefault()))
                        .andExpect(matchAll(
                                status().isConflict(),
                                jsonPath("$.code", Matchers.is((int) USER_EXISTS)),
                                jsonPath("$.message", Matchers.is(message)))
                        );
            }

            private void callEndPoint() throws Exception {
                when(messageSource.getMessage(eq("error.validation"), isNotNull(), any(Locale.class))).thenReturn(message);
                mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonedUserAccount()).locale(Locale.getDefault()))
                        .andExpect(matchAll(
                                status().isBadRequest(),
                                jsonPath("$.code", Matchers.is((int) VALIDATION)),
                                jsonPath("$.message", Matchers.is(message)),
                                jsonPath("validationErrors", hasItem(Matchers.containsString("username"))))
                        );
            }
        }
    }

    private String jsonedUserAccount() throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(userAccount);
        return json.replace("}", ",\"password\":\"ffffff3Q\"}");
    }
}