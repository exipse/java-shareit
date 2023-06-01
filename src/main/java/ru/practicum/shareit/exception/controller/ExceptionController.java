package ru.practicum.shareit.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailException(final EmailExistException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage());

    }

    @ExceptionHandler({MethodArgumentNotValidException.class, DataTimeValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Произошла ошибка валидации.");
    }

    @ExceptionHandler({ValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse noAvailable(final NoAvailableException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse userAccessException(final UserNoAccessException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dbconstrainException(final DBConstraintException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unsupported(final UnsupportedException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({UserNoFoundException.class, ItemNoFoundException.class,
            BookingNoFoundException.class, RequestNoFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Произошла непредвиденная ошибка."),
                HttpStatus.BAD_REQUEST
        );
    }
}
