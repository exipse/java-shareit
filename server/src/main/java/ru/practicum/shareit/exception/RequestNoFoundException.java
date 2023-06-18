package ru.practicum.shareit.exception;

public class RequestNoFoundException extends RuntimeException {

    public RequestNoFoundException(String message) {
        super(message);
    }
}
