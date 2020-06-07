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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import static com.ahmedsameha1.todo.Constants.ErrorCode.*;
import static com.ahmedsameha1.todo.Constants.SIGN_UP_URL;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UserAccountControllerUnitTest extends ProductionDatabaseBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountService userAccountService;

    @MockBean
    private MessageSource messageSource;

    private UserAccount userAccount;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String message = "message";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    @Test
    @DisplayName("Should pass because the sent request body is a json that represents a valid UserAccount")
    public void SignUp_test1() throws Exception {
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(userAccount);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isCreated(),
                        header().string("Location", "/user_account"),
                        jsonPath("$.username", Matchers.is(userAccount.getUsername())),
                        jsonPath("$.firstName", Matchers.is(userAccount.getFirstName())),
                        jsonPath("$.lastName", Matchers.is(userAccount.getLastName())),
                        jsonPath("$.gender", Matchers.is(userAccount.getGender().toString())),
                        jsonPath("$.email", Matchers.is(userAccount.getEmail())),
                        jsonPath("$.birthDay", Matchers.is(userAccount.getBirthDay().format(dateTimeFormatter)))
                ));
        verify(userAccountService).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Should fail because the sent request doesn't have a content type header of json")
    public void SignUp_test2() throws Exception {
        mockMvc.perform(post(SIGN_UP_URL)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isUnsupportedMediaType());
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_XML)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isUnsupportedMediaType());
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    @Test
    @DisplayName("Should fail because the sent request doesn't have a body")
    public void SignUp_test3() throws Exception {
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    @Test
    @DisplayName("Should fail because the sent request body isn't a valid json")
    public void SignUp_test4() throws Exception {
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount().replace("}", ")")).locale(Locale.getDefault()))
                .andExpect(status().isBadRequest());
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    @Test
    @DisplayName("Should fail because the Http method of the request is unsupported")
    public void SignUp_test5() throws Exception {
        mockMvc.perform(get(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isForbidden());
        mockMvc.perform(head(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isForbidden());
        mockMvc.perform(put(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isForbidden());
        mockMvc.perform(patch(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isForbidden());
        mockMvc.perform(options(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isForbidden());
        mockMvc.perform(request("TRACE", URI.create(SIGN_UP_URL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(request("VIEW", URI.create(SIGN_UP_URL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(request(UUID.randomUUID().toString(), URI.create(SIGN_UP_URL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount()).locale(Locale.getDefault()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should fail because there is an unsupported request parameter")
    public void SignUp_test6() throws Exception {
        when(messageSource.getMessage(eq("error.unsupportedRequestParameter"),
                isNotNull(), any(Locale.class))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount())
                .locale(Locale.getDefault())
                .param(message, message))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) UNSUPPORTED_REQUEST_PARAMETER)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(containsString(message)))
                ));
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount())
                .locale(Locale.getDefault())
                .param(message, message)
                .param("yyy", "kkk"))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) UNSUPPORTED_REQUEST_PARAMETER)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(containsString("yyy"))),
                        jsonPath("$.validationErrors", hasItem(containsString(message)))
                ));
    }

    @Test
    @DisplayName("Should pass because ignorance of UserAccount fields that not allowed to be handled by user input directly")
    public void SignUp_test7() throws Exception {
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(userAccount);
        var json = jsonedUserAccount().replace("}", ",\"enabled\":true}");
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isCreated(),
                        header().string("Location", "/user_account"),
                        jsonPath("$.username", Matchers.is(userAccount.getUsername())),
                        jsonPath("$.firstName", Matchers.is(userAccount.getFirstName())),
                        jsonPath("$.lastName", Matchers.is(userAccount.getLastName())),
                        jsonPath("$.gender", Matchers.is(userAccount.getGender().toString())),
                        jsonPath("$.email", Matchers.is(userAccount.getEmail())),
                        jsonPath("$.birthDay", Matchers.is(userAccount.getBirthDay().format(dateTimeFormatter)))
                ));
        verify(userAccountService).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Should pass because ignorance of unknown fields")
    public void SignUp_test8() throws Exception {
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(userAccount);
        var json = jsonedUserAccount().replace("}", ",\"unknown\":99}");
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isCreated(),
                        header().string("Location", "/user_account"),
                        jsonPath("$.username", Matchers.is(userAccount.getUsername())),
                        jsonPath("$.firstName", Matchers.is(userAccount.getFirstName())),
                        jsonPath("$.lastName", Matchers.is(userAccount.getLastName())),
                        jsonPath("$.gender", Matchers.is(userAccount.getGender().toString())),
                        jsonPath("$.email", Matchers.is(userAccount.getEmail())),
                        jsonPath("$.birthDay", Matchers.is(userAccount.getBirthDay().format(dateTimeFormatter)))
                ));
        verify(userAccountService).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't have a username")
    public void SignUp_Username_test1() throws Exception {
        userAccount.setUsername(null);
        callEndpoint("username");
        callEndpointRemoveNull("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an empty username")
    public void SignUp_Username_test2() throws Exception {
        userAccount.setUsername("");
        callEndpoint("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a username that contains only whitespace")
    public void SignUp_Username_test3() throws Exception {
        userAccount.setUsername("  ");
        callEndpoint("username");
        userAccount.setUsername("\n");
        callEndpoint("username");
        userAccount.setUsername("\r");
        callEndpoint("username");
        userAccount.setUsername("\t");
        callEndpoint("username");
        userAccount.setUsername("\t\n");
        callEndpoint("username");
        userAccount.setUsername("\t\r");
        callEndpoint("username");
        userAccount.setUsername("\r\n");
        callEndpoint("username");
        userAccount.setUsername("\r ");
        callEndpoint("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a username that has more than 50 characters")
    public void SignUp_Username_test4() throws Exception {
        userAccount.setUsername("f".repeat(51));
        callEndpoint("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a username that has whitespace")
    public void SignUp_Username_test5() throws Exception {
        userAccount.setUsername("t u");
        callEndpoint("username");
        userAccount.setUsername(" tu");
        callEndpoint("username");
        userAccount.setUsername("tu ");
        callEndpoint("username");
        userAccount.setUsername("tu\t");
        callEndpoint("username");
        userAccount.setUsername("\ntu ");
        callEndpoint("username");
        userAccount.setUsername("\tt u\r");
        callEndpoint("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a username that is already exists")
    public void SignUp_Username_test6() throws Exception {
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
        verify(userAccountService).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't have a password")
    public void SignUp_Password_test1() throws Exception {
        var json = jsonedUserAccount().replace(",\"password\":\"ffffff3Q\"}", "}");
        callEndpoint(json, "password");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that contains only whitespace")
    public void SignUp_Password_test2() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "                           ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\n\\t\\t\\t\\t\\n\\t\\t\\t\\t\\t\\t\\t");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t\\n\\t\\t\\t\\t\\n\\t\\t");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t \\t\\t\\t\\t \\t\\t");
        callEndpoint(json, "password");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that contains less than 8 characters")
    public void SignUp_Password_test3() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "fffff3Q");
        callEndpoint(json, "password");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that contains more than 255 characters")
    public void SignUp_Password_test4() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "f3Q" + "f".repeat(253));
        callEndpoint(json, "password");

    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that has whitespace")
    public void SignUp_Password_test5() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "tuaaaaaa aaaaaaaaaaaa");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", " tupppppppppppppppppppp");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqqqqqqqqqqqqqtu ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\rqqqqqqqqqqqqqqqqqqqqqqqqqtu ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\tqqqqqq\\rqqqqqqqqqqqqqqqqqqtu ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqq\\tqqqqqqqqqqqqqqqtu ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "\\rqqqqqqqqqqqqqqqqqqqqqqqtu\\n");
        callEndpoint(json, "password");
    }

    @Test
    @DisplayName("Should fail because the sent request body must be a json that has a password that has at least one lowercase character"
            + "and at least one uppercase character"
            + "and at least one digit")
    public void SignUp_Password_test6() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqq");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQQ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "333333333333");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqqQ");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqq3");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "33333333333q");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "33333333333Q");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQq");
        callEndpoint(json, "password");
        json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQ3");
        callEndpoint(json, "password");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has a firstName")
    public void SignUp_FirstName_test1() throws Exception {
        userAccount.setFirstName(null);
        callEndpoint("firstName");
        callEndpointRemoveNull("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an empty firstName")
    public void SignUp_FirstName_test2() throws Exception {
        userAccount.setFirstName("");
        callEndpoint("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a firstName that contains only whitespace")
    public void SignUp_FirstName_test3() throws Exception {
        userAccount.setFirstName("  ");
        callEndpoint("firstName");
        userAccount.setFirstName("\n");
        callEndpoint("firstName");
        userAccount.setFirstName("\r");
        callEndpoint("firstName");
        userAccount.setFirstName("\t");
        callEndpoint("firstName");
        userAccount.setFirstName("\t\r");
        callEndpoint("firstName");
        userAccount.setFirstName("\t\n");
        callEndpoint("firstName");
        userAccount.setFirstName("\t ");
        callEndpoint("firstName");
        userAccount.setFirstName(" \n");
        callEndpoint("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a firstName that has more than 100 characters")
    public void SignUp_FirstName_test4() throws Exception {
        userAccount.setFirstName("f".repeat(101));
        callEndpoint("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a firstName that has whitespace either at the start or at the end")
    public void SignUp_FirstName_test5() throws Exception {
        userAccount.setFirstName(" fff");
        callEndpoint("firstName");
        userAccount.setFirstName("fff ");
        callEndpoint("firstName");
        userAccount.setFirstName("\tfff");
        callEndpoint("firstName");
        userAccount.setFirstName("fff\t");
        callEndpoint("firstName");
        userAccount.setFirstName("\nfff");
        callEndpoint("firstName");
        userAccount.setFirstName("fff\n");
        callEndpoint("firstName");
        userAccount.setFirstName("\rfff");
        callEndpoint("firstName");
        userAccount.setFirstName("fff\r");
        callEndpoint("firstName");
        userAccount.setFirstName("\nfff\r");
        callEndpoint("firstName");
        userAccount.setFirstName(" fff\t");
        callEndpoint("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has a lastName")
    public void SignUp_LastName_test1() throws Exception {
        userAccount.setLastName(null);
        callEndpoint("lastName");
        callEndpointRemoveNull("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an empty lastName")
    public void SignUp_LastName_test2() throws Exception {
        userAccount.setLastName("");
        callEndpoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a lastName that contains only whitespace")
    public void SignUp_LastName_test3() throws Exception {
        userAccount.setLastName("  ");
        callEndpoint("lastName");
        userAccount.setLastName("\n");
        callEndpoint("lastName");
        userAccount.setLastName("\r");
        callEndpoint("lastName");
        userAccount.setLastName("\t");
        callEndpoint("lastName");
        userAccount.setLastName("\t\r");
        callEndpoint("lastName");
        userAccount.setLastName("\t\n");
        callEndpoint("lastName");
        userAccount.setLastName("\t ");
        callEndpoint("lastName");
        userAccount.setLastName(" \n");
        callEndpoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a lastName that has more than 100 characters")
    public void SignUp_LastName_test4() throws Exception {
        userAccount.setLastName("f".repeat(101));
        callEndpoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a lastName that has whitespace either at the start or at the end")
    public void SignUp_LastName_test5() throws Exception {
        userAccount.setLastName(" fff");
        callEndpoint("lastName");
        userAccount.setLastName("fff ");
        callEndpoint("lastName");
        userAccount.setLastName("\tfff");
        callEndpoint("lastName");
        userAccount.setLastName("fff\t");
        callEndpoint("lastName");
        userAccount.setLastName("\nfff");
        callEndpoint("lastName");
        userAccount.setLastName("fff\n");
        callEndpoint("lastName");
        userAccount.setLastName("\rfff");
        callEndpoint("lastName");
        userAccount.setLastName("fff\r");
        callEndpoint("lastName");
        userAccount.setLastName("\nfff\r");
        callEndpoint("lastName");
        userAccount.setLastName(" fff\t");
        callEndpoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has an email")
    public void SignUp_Email_test1() throws Exception {
        userAccount.setEmail(null);
        callEndpoint("email");
        callEndpointRemoveNull("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that contains only whitespace")
    public void SignUp_Email_test2() throws Exception {
        userAccount.setEmail("        ");
        callEndpoint("email");
        userAccount.setEmail("\n\n\n\n\n\n");
        callEndpoint("email");
        userAccount.setEmail("\r\r\r\r\r\r");
        callEndpoint("email");
        userAccount.setEmail("\t\t\t\t\t\t");
        callEndpoint("email");
        userAccount.setEmail("\t\r\t\r\t\t");
        callEndpoint("email");
        userAccount.setEmail("\t\n\t\t\n\t\t");
        callEndpoint("email");
        userAccount.setEmail("\t\t \t \t\t");
        callEndpoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that has more than 255 characters")
    public void SignUp_Email_test3() throws Exception {
        userAccount.setEmail("a@a.aa" + "a".repeat(250));
        callEndpoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that has whitespace")
    public void SignUp_Email_test4() throws Exception {
        userAccount.setEmail(" a@a.aa");
        callEndpoint("email");
        userAccount.setEmail("a@a.aa ");
        callEndpoint("email");
        userAccount.setEmail(" a@a .aa  ");
        callEndpoint("email");
        userAccount.setEmail("a\r@a.aa");
        callEndpoint("email");
        userAccount.setEmail("\ta@a.aa");
        callEndpoint("email");
        userAccount.setEmail("a @\na.aa");
        callEndpoint("email");
        userAccount.setEmail("\ra@a.aa\n");
        callEndpoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that doesn't conform to the specified regex")
    public void SignUp_Email_test5() throws Exception {
        userAccount.setEmail("aaaa.aa");
        callEndpoint("email");
        userAccount.setEmail("a@aaaa.");
        callEndpoint("email");
        userAccount.setEmail("a@a.a");
        callEndpoint("email");
        userAccount.setEmail("aaaaaaaa");
        callEndpoint("email");
        userAccount.setEmail("a.a@aa");
        callEndpoint("email");
        userAccount.setEmail(".aaa@aaa");
        callEndpoint("email");
        userAccount.setEmail("@aaa.aa");
        callEndpoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has a birthDay")
    public void SignUp_BirthDay_test1() throws Exception {
        userAccount.setBirthDay(null);
        callEndpoint("birthDay");
        callEndpointRemoveNull("birthDay");
    }

    @Test
    @DisplayName("Should fail because the sent body is a json that has birthDay that doesn't conform to the specified pattern")
    public void SignUp_BirthDay_test2() throws Exception {
        var json = jsonedUserAccount().replace("2010-10-10", "2010/10/10");
        BirthDay_callEndpoint(json);
        json = jsonedUserAccount().replace("2010-10-10", "10-10-2010");
        BirthDay_callEndpoint(json);
        json = jsonedUserAccount().replace("2010-10-10", "10/10/2010");
        BirthDay_callEndpoint(json);
        json = jsonedUserAccount().replace("2010-10-10", "2010-13-10");
        BirthDay_callEndpoint(json);
        json = jsonedUserAccount().replace("2010-10-10", "2010-10-32");
        BirthDay_callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the sent body is a json that has birthDay that isn't at the past")
    public void SignUp_BirthDay_test3() throws Exception {
        userAccount.setBirthDay(LocalDate.now().plusDays(1));
        callEndpoint("birthDay");
    }

    @Test
    @DisplayName("Should fail because the sent body is a json that has gender that isn't valid")
    public void SignUp_Gender_test1() throws Exception {
        var json = jsonedUserAccount().replace("MALE", "m");
        callEndpoint(json, "gender");
    }

    private void BirthDay_callEndpoint(String json) throws Exception {
        var suggestion = "suggestion";
        when(messageSource.getMessage(eq("error.datetimeValidation"), isNull(), eq(Locale.getDefault()))).thenReturn(message);
        when(messageSource.getMessage(eq("suggestion.datetimeValidation"), isNull(), eq(Locale.getDefault()))).thenReturn(suggestion);
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(null);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.code", Matchers.is((int) DATETIME_VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.suggestion", Matchers.is(suggestion))
                ));
        verify(userAccountService, never()).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    private String jsonedUserAccount() throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(userAccount);
        return json.replace("}", ",\"password\":\"ffffff3Q\"}");
    }

    private void callEndpoint(String field) throws Exception {
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), any(Locale.class))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount())
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(Matchers.containsString(field))))
                );
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    private void callEndpointRemoveNull(String field) throws Exception {
        var json = jsonedUserAccount();
        json = json.replace("\"" + field + "\":null,", "");
        json = json.replace("\"" + field + "\":null", "");
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), any(Locale.class))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(Matchers.containsString(field))))
                );
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    private void callEndpoint(String json, String field) throws Exception {
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), eq(Locale.getDefault()))).thenReturn(message);
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(null);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(Matchers.containsString(field))))
                );
        verify(userAccountService, never()).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }
}