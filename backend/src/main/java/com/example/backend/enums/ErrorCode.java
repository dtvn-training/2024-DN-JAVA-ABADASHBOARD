package com.example.backend.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //code according to httpStatus standard and may be added in the future
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    CONFLICT(409, "Conflict", HttpStatus.CONFLICT),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity", HttpStatus.UNPROCESSABLE_ENTITY),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED(501, "Not Implemented", HttpStatus.NOT_IMPLEMENTED),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
