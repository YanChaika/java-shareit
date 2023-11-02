package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void handleNotFoundException() {
        ErrorResponse actualError = errorHandler.handleNotFoundException(new NotFoundException("error"));

        assertEquals("error", actualError.getError());
    }

    @Test
    void handleValidationException() {
        ErrorResponse actualError = errorHandler.handleValidationException(new ValidationException("error"));

        assertEquals("error", actualError.getError());
    }
}