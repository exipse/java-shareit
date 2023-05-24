package ru.practicum.shareit.exception;

public class DBConstraintException extends RuntimeException{

    public DBConstraintException(String message) {
        super(message);
    }
}
