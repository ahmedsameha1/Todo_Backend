package com.ahmedsameha1.todo.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter @Getter
public class ErrorResponse {
    private Instant timestamp = Instant.now();
    private String path;
    private String message;
    private String suggestion;
}
