package com.ahmedsameha1.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This is a user account with the same username")
public class UserExistsException extends RuntimeException {
}
