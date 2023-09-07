package ru.practicum.shareit.exceptions;

public class AlreadyPresentException extends RuntimeException {

    public AlreadyPresentException(final String message) {
        super(message);
    }
}
