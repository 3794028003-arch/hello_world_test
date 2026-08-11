package com.memo.backend.common.exception;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException() {
        super("Password is incorrect");
    }
}
