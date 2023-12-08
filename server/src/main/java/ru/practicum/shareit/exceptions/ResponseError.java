package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class ResponseError {
    private final String error;

    public ResponseError(String message) {
        this.error = message;
    }
}
