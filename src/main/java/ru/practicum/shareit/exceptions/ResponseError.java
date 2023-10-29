package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseError {
    private final HttpStatus status;
    private final String message;

    public ResponseError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
