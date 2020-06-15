package com.ahmedsameha1.todo.web.controller;

import com.ahmedsameha1.todo.domain_model.ProductionDatabaseBaseTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static com.ahmedsameha1.todo.Constants.ErrorCode.REQUEST_BODY_VALIDATION;
import static com.ahmedsameha1.todo.Constants.REGEX_FOR_NON_FULLY_QUALIFIED_CLASS_NAME_MESSAGE;
import static com.ahmedsameha1.todo.Constants.SIGN_UP_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class GenericWebControllerTest extends ProductionDatabaseBaseTest {
    private static final String message = "message";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageSource messageSource;

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "There is a missed comma between two entries")
    public void test1() throws Exception {
        var json = "{\"firstName\":\"user1\"\"lastName\":\"value\"}";
        when(messageSource.getMessage(eq("suggestion.requestBodyValidation"), isNull(), eq(Locale.getDefault()))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) REQUEST_BODY_VALIDATION)),
                        jsonPath("$.message", allOf(containsString("comma"), Matchers.matchesPattern(REGEX_FOR_NON_FULLY_QUALIFIED_CLASS_NAME_MESSAGE))),
                        jsonPath("$.suggestion", Matchers.is(message))
                ));
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing initial opening brace")
    public void test2() throws Exception {
        var json = "\"firstName\":\"user1\",\"lastName\":\"value\"}";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing double quotation")
    public void test3() throws Exception {
        var json = "{firstName\":\"user1\",\"lastName\":\"value\"}";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing colon")
    public void test4() throws Exception {
        var json = "{\"firstName\"\"user1\",\"lastName\":\"value\"}";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Starts with [")
    public void test5() throws Exception {
        var json = "[\"firstName\":\"user1\",\"lastName\":\"value\"}";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Starts with (")
    public void test6() throws Exception {
        var json = "(\"firstName\":\"user1\",\"lastName\":\"value\"}";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Starts with <")
    public void test7() throws Exception {
        var json = "<\"firstName\":\"user1\",\"lastName\":\"value\"}";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing closing brace")
    public void test8() throws Exception {
        var json = "{\"firstName\":\"user1\",\"lastName\":\"value\"";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json")
    public void test9() throws Exception {
        var json = "{\"firstName\":\"user1\",\"lastName\":\"value\"]";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing closing brace")
    public void test10() throws Exception {
        var json = "{\"firstName\":\"user1\",\"lastName\":\"value\")";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing closing brace")
    public void test11() throws Exception {
        var json = "{\"firstName\":\"user1\",\"lastName\":\"value\">";
        callEndpoint(json);
    }

    @Test
    @DisplayName("Should fail because the request body is invalid json. "
            + "Missing closing brace")
    public void test12() throws Exception {
        var json = "[\"firstName\":\"user1\",\"lastName\":\"value\"]";
        callEndpoint(json);
    }

    private void callEndpoint(String json) throws Exception {
        when(messageSource.getMessage(eq("suggestion.requestBodyValidation"), isNull(), eq(Locale.getDefault()))).thenReturn(message);
        mockMvc.perform(post(SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).locale(Locale.getDefault()))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code", Matchers.is((int) REQUEST_BODY_VALIDATION)),
                        jsonPath("$.message", matchesPattern(REGEX_FOR_NON_FULLY_QUALIFIED_CLASS_NAME_MESSAGE)),
                        jsonPath("$.suggestion", Matchers.is(message))
                ));
    }
}
