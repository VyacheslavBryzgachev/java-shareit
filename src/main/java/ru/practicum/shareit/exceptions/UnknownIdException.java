package ru.practicum.shareit.exceptions;

public class UnknownIdException extends RuntimeException {

    public UnknownIdException(String message) {
        super(message);
    }
}
