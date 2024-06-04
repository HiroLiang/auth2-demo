package com.tfs.auth.model.exception;

public class BaseException extends RuntimeException {

    public BaseException(String errorMessage) {
        super(errorMessage);
    }
}
