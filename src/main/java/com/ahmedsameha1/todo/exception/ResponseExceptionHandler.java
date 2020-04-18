package com.ahmedsameha1.todo.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.ahmedsameha1.todo.security.Constants.ErrorCode.BAD_EMAIL_VERIFICATION_TOKEN;

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

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> userAccountLocked(HttpServletRequest request) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(messageSource.getMessage("error.userAccountLockedProblem",
                null, request.getLocale()));
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.LOCKED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> wrongPassword(HttpServletRequest request) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(messageSource.getMessage("error.badCredentialsProblem",
                null, request.getLocale()));
        errorResponse.setSuggestion(messageSource.getMessage("error.badCredentialsSuggestion",
                null, request.getLocale()));
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadEmailVerificationTokenException.class)
    public ResponseEntity<ErrorResponse> badEmailVerificationToken(HttpServletRequest request) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(messageSource.getMessage("error.badEmailVerificationTokenProblem",
                null, request.getLocale()));
        errorResponse.setSuggestion(messageSource.getMessage("error.badEmailVerificationTokenSuggestion",
                null, request.getLocale()));
        errorResponse.setCode(BAD_EMAIL_VERIFICATION_TOKEN);
        errorResponse.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
