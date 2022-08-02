package ru.practicum.shareit.errorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final ItemAvailableException e) {
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler({ItemNotFoundException.class,
            UserNotFoundException.class,
            RequestNotFoundException.class,
            BookingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final Exception e) {
        return new ErrorResponse("error", e.getMessage());
    }


    @ExceptionHandler({ItemValidException.class,
            UserValidException.class,
            RequestValidException.class,
            BookingValidException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final Exception e) {
        return new ErrorResponse("error", e.getMessage());
    }
}
