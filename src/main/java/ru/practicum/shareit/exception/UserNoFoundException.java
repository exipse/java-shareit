package ru.practicum.shareit.exception;

public class UserNoFoundException extends RuntimeException {

    public UserNoFoundException(String message) {
        super(message);
    }
}
