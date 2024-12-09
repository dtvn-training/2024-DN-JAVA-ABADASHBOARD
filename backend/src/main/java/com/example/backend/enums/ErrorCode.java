package com.example.backend.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    NCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    EXISTED(1002, "Existed", HttpStatus.BAD_REQUEST),
    NOT_EXISTED(1003, "Not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
    LOCKED(1006, "This account was locked", HttpStatus.BAD_REQUEST),

    PASSWORD_INCORRECT(1999, "Password not correct", HttpStatus.BAD_REQUEST),
    NOT_BLANK(2001, "This field is mandatory", HttpStatus.BAD_REQUEST),
    BETWEEN_SIZE(2002, "This field must be between {min} and {max} characters", HttpStatus.BAD_REQUEST),
    INVALID(2003, "This field is invalid", HttpStatus.BAD_REQUEST),
    MIN_SIZE(2004, "This field must be at least {min} characters long", HttpStatus.BAD_REQUEST),
    DATE_FUTURE(2008, "Date must be a future date", HttpStatus.BAD_REQUEST),
    NOT_NULL(2009, "This field must not be null", HttpStatus.BAD_REQUEST),
    POSITIVE(2010, "This field must be greater than 0", HttpStatus.BAD_REQUEST),
    MAX_SIZE(2011, "This field must be at max {max} characters long", HttpStatus.BAD_REQUEST),
    DATE_FUTURE_OR_PRESENT(2008, "Date must be a future date or today  ", HttpStatus.BAD_REQUEST),
    SCORE_MIN_1(2012, "Score value must be greater than or equal to 1", HttpStatus.BAD_REQUEST),
    SCORE_MAX_5(2013, "Score value must be less than or equal to 5", HttpStatus.BAD_REQUEST),
    POSITIVE_OR_ZERO(2014, "This field must be greater or equal to 0", HttpStatus.BAD_REQUEST),
    BAD_REQUEST(2015, "Request error", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
