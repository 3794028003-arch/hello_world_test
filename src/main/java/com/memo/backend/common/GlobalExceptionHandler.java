package com.memo.backend.common;

import com.memo.backend.common.exception.MemoNotFoundException;
import com.memo.backend.common.exception.PasswordIncorrectException;
import com.memo.backend.common.exception.UserAlreadyExistsException;
import com.memo.backend.common.exception.UserNotFoundException;
import com.memo.backend.common.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ApiError> handleConflict(UserAlreadyExistsException exception) {
        return error(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", "用户名已存在");
    }

    @ExceptionHandler({UserNotFoundException.class, MemoNotFoundException.class})
    ResponseEntity<ApiError> handleNotFound(RuntimeException exception) {
        return error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    ResponseEntity<ApiError> handleUnauthorized(PasswordIncorrectException exception) {
        return error(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "用户名或密码错误");
    }

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ApiError> handleInvalidToken(InvalidTokenException exception) {
        return error(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage()).orElse("Validation failed");
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(new ApiError(code, message, Instant.now()));
    }
}
