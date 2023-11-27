package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    @Mock
    ErrorHandler errorHandler;

    @Test
    void handleUnknownIdExceptionTest() {
        ResponseError error = new ResponseError("Unknown Id");
        UnknownIdException exception = new UnknownIdException("Unknown Id");
        when(errorHandler.handleUnknownIdException(exception))
                .thenReturn(error);

        ResponseError result = errorHandler.handleUnknownIdException(exception);
        Assertions.assertEquals(exception.getMessage(), result.getError());
        verify(errorHandler, times(1)).handleUnknownIdException(exception);
    }

    @Test
    void handleBookingExceptionTest() {
        ResponseError error = new ResponseError("Booking Error");
        BookingException exception = new BookingException("Booking Error");
        when(errorHandler.handleBookingException(exception))
                .thenReturn(error);

        ResponseError result = errorHandler.handleBookingException(exception);
        Assertions.assertEquals(exception.getMessage(), result.getError());
        verify(errorHandler, times(1)).handleBookingException(exception);
    }

    @Test
    void handleWrongStateExceptionTest() {
        ResponseError error = new ResponseError("Wrong State");
        WrongStateException exception = new WrongStateException("Wrong State");
        when(errorHandler.handleWrongStateException(exception))
                .thenReturn(error);

        ResponseError result = errorHandler.handleWrongStateException(exception);
        Assertions.assertEquals(exception.getMessage(), result.getError());
        verify(errorHandler, times(1)).handleWrongStateException(exception);
    }
}
