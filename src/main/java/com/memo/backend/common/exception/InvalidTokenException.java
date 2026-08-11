package com.memo.backend.common.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() { super("Refresh token is invalid or expired"); }
}
