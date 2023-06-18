package ru.practicum.shareit.exception;

public class BookingNoFoundException extends RuntimeException {

    public BookingNoFoundException(String message) {
        super(message);
    }
}
