package com.ahmedsameha1.todo.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> sameUserExists(HttpServletRequest request) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(messageSource.getMessage("error.userExistsProblem",
                null, request.getLocale()));
        errorResponse.setSuggestion(messageSource.getMessage("error.userExistsSuggestion",
                null, request.getLocale()));
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

