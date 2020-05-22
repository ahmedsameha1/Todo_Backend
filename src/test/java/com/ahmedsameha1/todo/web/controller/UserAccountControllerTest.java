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
                callEndPoint("username");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has an empty username")
            public void test2() throws Exception {
                userAccount.setUsername("");
                callEndPoint("username");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that contains only whitespace")
            public void test3() throws Exception {
                userAccount.setUsername("  ");
                callEndPoint("username");
                userAccount.setUsername("\n");
                callEndPoint("username");
                userAccount.setUsername("\r");
                callEndPoint("username");
                userAccount.setUsername("\t");
                callEndPoint("username");
                userAccount.setUsername("\t\n");
                callEndPoint("username");
                userAccount.setUsername("\t\r");
                callEndPoint("username");
                userAccount.setUsername("\r\n");
                callEndPoint("username");
                userAccount.setUsername("\r ");
                callEndPoint("username");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that has more than 50 characters")
            public void test4() throws Exception {
                userAccount.setUsername("f".repeat(51));
                callEndPoint("username");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a username that has whitespace")
            public void test5() throws Exception {
                userAccount.setUsername("t u");
                callEndPoint("username");
                userAccount.setUsername(" tu");
                callEndPoint("username");
                userAccount.setUsername("tu ");
                callEndPoint("username");
                userAccount.setUsername("tu\t");
                callEndPoint("username");
                userAccount.setUsername("\ntu ");
                callEndPoint("username");
                userAccount.setUsername("\tt u\r");
                callEndPoint("username");
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
        }

        @Nested
        @DisplayName("Password tests")
        class Password {
            @Test
            @DisplayName("Should fail because the sent request body is a json that doesn't have a password")
            public void test1() throws Exception {
                var json = jsonedUserAccount().replace(",\"password\":\"ffffff3Q\"}", "}");
                callEndpoint(json);
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has password that contains only whitespace")
            public void test2() throws Exception {
                var json = jsonedUserAccount().replace("ffffff3Q", "                           ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\n\\t\\t\\t\\t\\n\\t\\t\\t\\t\\t\\t\\t");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t\\n\\t\\t\\t\\t\\n\\t\\t");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t \\t\\t\\t\\t \\t\\t");
                callEndpoint(json);
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has password that contains less than 8 characters")
            public void test3() throws Exception {
                var json = jsonedUserAccount().replace("ffffff3Q", "fffff3Q");
                callEndpoint(json);
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has password that contains more than 255 characters")
            public void test4() throws Exception {
                var json = jsonedUserAccount().replace("ffffff3Q", "f3Q" + "f".repeat(253));
                callEndpoint(json);

            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has password that has whitespace")
            public void test5() throws Exception {
                var json = jsonedUserAccount().replace("ffffff3Q", "tuaaaaaa aaaaaaaaaaaa");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", " tupppppppppppppppppppp");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqqqqqqqqqqqqqtu ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\rqqqqqqqqqqqqqqqqqqqqqqqqqtu ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\tqqqqqq\\rqqqqqqqqqqqqqqqqqqtu ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqq\\tqqqqqqqqqqqqqqqtu ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "\\rqqqqqqqqqqqqqqqqqqqqqqqtu\\n");
                callEndpoint(json);
            }

            @Test
            @DisplayName("Should fail because the sent request body must be a json that has a password that has at least one lowercase character"
                    + "and at least one uppercase character"
                    + "and at least one digit")
            public void test7() throws Exception {
                var json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqq");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQQ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "333333333333");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqqQ");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqq3");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "33333333333q");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "33333333333Q");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQq");
                callEndpoint(json);
                json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQ3");
                callEndpoint(json);
            }

            private void callEndpoint(String json) throws Exception {
                when(messageSource.getMessage(eq("error.validation"), isNotNull(), eq(Locale.getDefault()))).thenReturn(message);
                mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).locale(Locale.getDefault()))
                        .andExpect(matchAll(
                                status().isBadRequest(),
                                jsonPath("$.code", Matchers.is((int) VALIDATION)),
                                jsonPath("$.message", Matchers.is(message)),
                                jsonPath("validationErrors", hasItem(Matchers.containsString("password"))))
                        );
            }
        }

        @Nested
        class FirstName {
            @Test
            @DisplayName("Should fail because the sent request body is a json that doesn't has a firstName")
            public void test1() throws Exception {
                userAccount.setFirstName(null);
                callEndPoint("firstName");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has an empty firstName")
            public void test2() throws Exception {
               userAccount.setFirstName("");
               callEndPoint("firstName");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a firstName that contains only whitespace")
            public void test3() throws Exception {
                userAccount.setFirstName("  ");
                callEndPoint("firstName");
                userAccount.setFirstName("\n");
                callEndPoint("firstName");
                userAccount.setFirstName("\r");
                callEndPoint("firstName");
                userAccount.setFirstName("\t");
                callEndPoint("firstName");
                userAccount.setFirstName("\t\r");
                callEndPoint("firstName");
                userAccount.setFirstName("\t\n");
                callEndPoint("firstName");
                userAccount.setFirstName("\t ");
                callEndPoint("firstName");
                userAccount.setFirstName(" \n");
                callEndPoint("firstName");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a firstName that has more than 100 characters")
            public void test4() throws Exception {
                userAccount.setFirstName("f".repeat(101));
                callEndPoint("firstName");
            }

            @Test
            @DisplayName("Should fail because the sent request body is a json that has a firstName that has whitespace either at the start or at the end")
            public void test5() throws Exception {
                userAccount.setFirstName(" fff");
                callEndPoint("firstName");
                userAccount.setFirstName("fff ");
                callEndPoint("firstName");
                userAccount.setFirstName("\tfff");
                callEndPoint("firstName");
                userAccount.setFirstName("fff\t");
                callEndPoint("firstName");
                userAccount.setFirstName("\nfff");
                callEndPoint("firstName");
                userAccount.setFirstName("fff\n");
                callEndPoint("firstName");
                userAccount.setFirstName("\rfff");
                callEndPoint("firstName");
                userAccount.setFirstName("fff\r");
                callEndPoint("firstName");
                userAccount.setFirstName("\nfff\r");
                callEndPoint("firstName");
                userAccount.setFirstName(" fff\t");
                callEndPoint("firstName");
            }
        }
    }

    private String jsonedUserAccount() throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(userAccount);
        return json.replace("}", ",\"password\":\"ffffff3Q\"}");
    }

    private void callEndPoint(String field) throws Exception {
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), any(Locale.class))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("validationErrors", hasItem(Matchers.containsString(field))))
                );
    }
}