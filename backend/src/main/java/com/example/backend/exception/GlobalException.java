package com.example.backend.exception;

import com.example.backend.enums.ErrorCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiException handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errors =  new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ApiException.builder()
                .code(ErrorCode.NOT_NULL.getCode())
                .message(errors.toString())
                .build();
    }
}
