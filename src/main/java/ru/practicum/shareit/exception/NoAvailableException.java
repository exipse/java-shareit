package ru.practicum.shareit.exception;

public class NoAvailableException extends RuntimeException{
    public NoAvailableException(String message) {
        super(message);
    }
}
