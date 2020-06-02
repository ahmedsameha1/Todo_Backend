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

import static com.ahmedsameha1.todo.Constants.ErrorCode.USER_EXISTS;
import static com.ahmedsameha1.todo.Constants.ErrorCode.VALIDATION;
import static com.ahmedsameha1.todo.Constants.SIGN_UP_URL;
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

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    @Test
    @DisplayName("Should pass because ignorance of UserAccount fields that not allowed to be handled by user input directly")
    public void SignUp_test6() throws Exception {
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
    public void SignUp_test7() throws Exception {
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
        callEndPoint("username");
        callEndPointIgnoreNull("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an empty username")
    public void SignUp_Username_test2() throws Exception {
        userAccount.setUsername("");
        callEndPoint("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a username that contains only whitespace")
    public void SignUp_Username_test3() throws Exception {
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
    public void SignUp_Username_test4() throws Exception {
        userAccount.setUsername("f".repeat(51));
        callEndPoint("username");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a username that has whitespace")
    public void SignUp_Username_test5() throws Exception {
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
        Password_callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that contains only whitespace")
    public void SignUp_Password_test2() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "                           ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\n\\t\\t\\t\\t\\n\\t\\t\\t\\t\\t\\t\\t");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t\\n\\t\\t\\t\\t\\n\\t\\t");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\t\\t\\t\\t\\t\\t\\t \\t\\t\\t\\t \\t\\t");
        Password_callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that contains less than 8 characters")
    public void SignUp_Password_test3() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "fffff3Q");
        Password_callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that contains more than 255 characters")
    public void SignUp_Password_test4() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "f3Q" + "f".repeat(253));
        Password_callEndpoint(json);

    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has password that has whitespace")
    public void SignUp_Password_test5() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "tuaaaaaa aaaaaaaaaaaa");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", " tupppppppppppppppppppp");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqqqqqqqqqqqqqtu ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\rqqqqqqqqqqqqqqqqqqqqqqqqqtu ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\tqqqqqq\\rqqqqqqqqqqqqqqqqqqtu ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqq\\tqqqqqqqqqqqqqqqtu ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "\\rqqqqqqqqqqqqqqqqqqqqqqqtu\\n");
        Password_callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the sent request body must be a json that has a password that has at least one lowercase character"
            + "and at least one uppercase character"
            + "and at least one digit")
    public void SignUp_Password_test7() throws Exception {
        var json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqq");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQQ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "333333333333");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqqQ");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "qqqqqqqqqqqq3");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "33333333333q");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "33333333333Q");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQq");
        Password_callEndpoint(json);
        json = jsonedUserAccount().replace("ffffff3Q", "QQQQQQQQQQQ3");
        Password_callEndpoint(json);
    }

    private void Password_callEndpoint(String json) throws Exception {
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), eq(Locale.getDefault()))).thenReturn(message);
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(null);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(Matchers.containsString("password"))))
                );
        verify(userAccountService, never()).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has a firstName")
    public void SignUp_FirstName_test1() throws Exception {
        userAccount.setFirstName(null);
        callEndPoint("firstName");
        callEndPointIgnoreNull("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an empty firstName")
    public void SignUp_FirstName_test2() throws Exception {
        userAccount.setFirstName("");
        callEndPoint("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a firstName that contains only whitespace")
    public void SignUp_FirstName_test3() throws Exception {
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
    public void SignUp_FirstName_test4() throws Exception {
        userAccount.setFirstName("f".repeat(101));
        callEndPoint("firstName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a firstName that has whitespace either at the start or at the end")
    public void SignUp_FirstName_test5() throws Exception {
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

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has a lastName")
    public void SignUp_LastName_test1() throws Exception {
        userAccount.setLastName(null);
        callEndPoint("lastName");
        callEndPointIgnoreNull("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an empty lastName")
    public void SignUp_LastName_test2() throws Exception {
        userAccount.setLastName("");
        callEndPoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a lastName that contains only whitespace")
    public void SignUp_LastName_test3() throws Exception {
        userAccount.setLastName("  ");
        callEndPoint("lastName");
        userAccount.setLastName("\n");
        callEndPoint("lastName");
        userAccount.setLastName("\r");
        callEndPoint("lastName");
        userAccount.setLastName("\t");
        callEndPoint("lastName");
        userAccount.setLastName("\t\r");
        callEndPoint("lastName");
        userAccount.setLastName("\t\n");
        callEndPoint("lastName");
        userAccount.setLastName("\t ");
        callEndPoint("lastName");
        userAccount.setLastName(" \n");
        callEndPoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a lastName that has more than 100 characters")
    public void SignUp_LastName_test4() throws Exception {
        userAccount.setLastName("f".repeat(101));
        callEndPoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has a lastName that has whitespace either at the start or at the end")
    public void SignUp_LastName_test5() throws Exception {
        userAccount.setLastName(" fff");
        callEndPoint("lastName");
        userAccount.setLastName("fff ");
        callEndPoint("lastName");
        userAccount.setLastName("\tfff");
        callEndPoint("lastName");
        userAccount.setLastName("fff\t");
        callEndPoint("lastName");
        userAccount.setLastName("\nfff");
        callEndPoint("lastName");
        userAccount.setLastName("fff\n");
        callEndPoint("lastName");
        userAccount.setLastName("\rfff");
        callEndPoint("lastName");
        userAccount.setLastName("fff\r");
        callEndPoint("lastName");
        userAccount.setLastName("\nfff\r");
        callEndPoint("lastName");
        userAccount.setLastName(" fff\t");
        callEndPoint("lastName");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has an email")
    public void SignUp_Email_test1() throws Exception {
        userAccount.setEmail(null);
        callEndPoint("email");
        callEndPointIgnoreNull("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that contains only whitespace")
    public void SignUp_Email_test2() throws Exception {
        userAccount.setEmail("        ");
        callEndPoint("email");
        userAccount.setEmail("\n\n\n\n\n\n");
        callEndPoint("email");
        userAccount.setEmail("\r\r\r\r\r\r");
        callEndPoint("email");
        userAccount.setEmail("\t\t\t\t\t\t");
        callEndPoint("email");
        userAccount.setEmail("\t\r\t\r\t\t");
        callEndPoint("email");
        userAccount.setEmail("\t\n\t\t\n\t\t");
        callEndPoint("email");
        userAccount.setEmail("\t\t \t \t\t");
        callEndPoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that has more than 255 characters")
    public void SignUp_Email_test3() throws Exception {
        userAccount.setEmail("a@a.aa" + "a".repeat(250));
        callEndPoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that has whitespace")
    public void SignUp_Email_test4() throws Exception {
        userAccount.setEmail(" a@a.aa");
        callEndPoint("email");
        userAccount.setEmail("a@a.aa ");
        callEndPoint("email");
        userAccount.setEmail(" a@a .aa  ");
        callEndPoint("email");
        userAccount.setEmail("a\r@a.aa");
        callEndPoint("email");
        userAccount.setEmail("\ta@a.aa");
        callEndPoint("email");
        userAccount.setEmail("a @\na.aa");
        callEndPoint("email");
        userAccount.setEmail("\ra@a.aa\n");
        callEndPoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that has an email that doesn't conform to the specified regex")
    public void SignUp_Email_test5() throws Exception {
        userAccount.setEmail("aaaa.aa");
        callEndPoint("email");
        userAccount.setEmail("a@aaaa.");
        callEndPoint("email");
        userAccount.setEmail("a@a.a");
        callEndPoint("email");
        userAccount.setEmail("aaaaaaaa");
        callEndPoint("email");
        userAccount.setEmail("a.a@aa");
        callEndPoint("email");
        userAccount.setEmail(".aaa@aaa");
        callEndPoint("email");
        userAccount.setEmail("@aaa.aa");
        callEndPoint("email");
    }

    @Test
    @DisplayName("Should fail because the sent request body is a json that doesn't has a birthDay")
    public void SignUp_BirthDay_test1() throws Exception {
        userAccount.setBirthDay(null);
        callEndPoint("birthDay");
        callEndPointIgnoreNull("birthDay");
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
        callEndPoint("birthDay");
    }

    private void BirthDay_callEndpoint(String json) throws Exception {
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), eq(Locale.getDefault()))).thenReturn(message);
        when(userAccountService.registerUserAccount(eq(userAccount), any(HttpServletRequest.class))).thenReturn(null);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .locale(Locale.getDefault()))
                .andExpect(status().isBadRequest());
        verify(userAccountService, never()).registerUserAccount(eq(userAccount), any(HttpServletRequest.class));
    }

    private String jsonedUserAccount() throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(userAccount);
        return json.replace("}", ",\"password\":\"ffffff3Q\"}");
    }

    private void callEndPoint(String field) throws Exception {
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), any(Locale.class))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonedUserAccount())
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(Matchers.containsString(field))))
                );
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }

    private void callEndPointIgnoreNull(String field) throws Exception {
        var json = jsonedUserAccount();
        json = json.replace("\"" + field + "\":null,", "");
        json = json.replace("\"" + field + "\":null", "");
        when(messageSource.getMessage(eq("error.validation"), isNotNull(), any(Locale.class))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) VALIDATION)),
                        jsonPath("$.message", Matchers.is(message)),
                        jsonPath("$.validationErrors", hasItem(Matchers.containsString(field))))
                );
        verify(userAccountService, never()).registerUserAccount(any(), any());
    }
}