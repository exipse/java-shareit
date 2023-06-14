package ru.practicum.shareit.exception;

public class UserNoAccessException extends RuntimeException {

    public UserNoAccessException(String message) {
        super(message);
    }
}
