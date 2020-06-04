package com.ahmedsameha1.todo.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.ahmedsameha1.todo.Constants.ErrorCode.*;

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
        errorResponse.setCode(USER_EXISTS);
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
    public ResponseEntity<ErrorResponse> wrongUsernamePasswordCombination(HttpServletRequest request) {
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

    @ExceptionHandler(ExpiredEmailVerificationTokenException.class)
    public ResponseEntity<ErrorResponse> expiredEmailVerificationTokenException(HttpServletRequest httpServletRequest) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(messageSource.getMessage("error.ExpiredEmailVerificationTokenProblem",
                null, httpServletRequest.getLocale()));
        errorResponse.setSuggestion(messageSource.getMessage("error.ExpiredEmailVerificationTokenSuggestion",
                null, httpServletRequest.getLocale()));
        errorResponse.setCode(EXPIRED_EMAIL_VERIFICATION_TOKEN);
        errorResponse.setPath(httpServletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> disabledUserAccount(HttpServletRequest httpServletRequest) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(messageSource.getMessage("error.DisabledExceptionProblem",
                null, httpServletRequest.getLocale()));
        errorResponse.setSuggestion(messageSource.getMessage("error.DisabledExceptionSuggestion",
                null, httpServletRequest.getLocale()));
        errorResponse.setCode(DISABLED_USER_ACCOUNT);
        errorResponse.setPath(httpServletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                 HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errors = exception.getBindingResult().getAllErrors();
        var errorResponse = new ErrorResponse();
        errorResponse.setCode(VALIDATION);
        errorResponse.setMessage(messageSource.getMessage("error.validation",
                new Integer[]{errors.size()}, request.getLocale()));
        errors.forEach(objectError -> {
            if (objectError instanceof FieldError) {
                errorResponse.getValidationErrors().add(((FieldError) objectError).getField() + ": " + objectError.getDefaultMessage());
            } else {
                errorResponse.getValidationErrors().add(objectError.getDefaultMessage());
            }
        });
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getCause() instanceof JsonMappingException) {
            var errorResponse = new ErrorResponse();
            JsonMappingException jsonMappingException = (JsonMappingException) ex.getCause();
            jsonMappingException.getPath().forEach(reference ->
                    errorResponse.getValidationErrors().add(reference.getFieldName()));
            errorResponse.setCode(VALIDATION);
            errorResponse.setMessage(messageSource.getMessage("error.validation",
                    new Integer[]{jsonMappingException.getPath().size()}, request.getLocale()));
            return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
           return super.handleHttpMessageNotReadable(ex, headers, status, request);
        }
    }

    @ExceptionHandler(UnsupportedRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedRequestParameterException
            (UnsupportedRequestParameterException unsupportedRequestParameterException,
             HttpServletRequest httpServletRequest) {
        var errorResponse = new ErrorResponse();
        errorResponse.setCode(UNSUPPORTED_REQUEST_PARAMETER);
        var message = messageSource.getMessage("error.unsupportedRequestParameter",
                new Integer[]{unsupportedRequestParameterException.getUnsupportedRequestParameters().size()},
                httpServletRequest.getLocale());
        errorResponse.setMessage(message);
        unsupportedRequestParameterException.getUnsupportedRequestParameters()
                .forEach(parameter -> errorResponse.getValidationErrors().add(parameter));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
