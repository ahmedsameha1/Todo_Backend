package com.ahmedsameha1.todo.exception;

import lombok.Getter;

import java.util.List;

public class UnsupportedRequestParameterException extends RuntimeException {
    @Getter
    private final List<String> unsupportedRequestParameters;

    public UnsupportedRequestParameterException(List<String> unsupportedRequestParameters) {
        this.unsupportedRequestParameters = unsupportedRequestParameters;
    }
}
