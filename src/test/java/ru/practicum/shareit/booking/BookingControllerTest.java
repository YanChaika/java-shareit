package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.awt.print.Book;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingServiceImpl bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void getAll_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        List<BookingFullDto> expectedBookingFullDto = List.of(new BookingFullDto());
        when(bookingService.getAllByUserId(userId, from, size)).thenReturn(expectedBookingFullDto);

        ResponseEntity<List<BookingFullDto>> response = bookingController.getAll(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingFullDto, response.getBody());
    }
}