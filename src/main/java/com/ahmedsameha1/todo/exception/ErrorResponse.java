package com.ahmedsameha1.todo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter @Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private Instant timestamp = Instant.now();
    private String path;
    private short code;
    private String message;
    private String suggestion;
}
