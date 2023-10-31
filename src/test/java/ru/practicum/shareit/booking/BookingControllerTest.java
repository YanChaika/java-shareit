package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

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
    void createBooking_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        BookingDto booking = new BookingDto();
        BookingFullDto expectedBookingDto = new BookingFullDto();
        when(bookingService.create(booking, userId)).thenReturn(expectedBookingDto);

        ResponseEntity<BookingFullDto> response = bookingController.createBooking(userId, booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingDto, response.getBody());
    }

    @Test
    void updateBooking_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        Long bookingId = 0L;
        String approved = "true";
        BookingFullDto expectedBooking = new BookingFullDto();
        when(bookingService.update(approved, bookingId, userId)).thenReturn(expectedBooking);

        ResponseEntity<BookingFullDto> response = bookingController.updateBooking(userId, bookingId, approved);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }
    @Test
    void getBookingById_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        Long bookingId = 0L;
        BookingFullDto expectedBooking = new BookingFullDto();
        when(bookingService.get(userId, bookingId)).thenReturn(expectedBooking);

        ResponseEntity<BookingFullDto> response = bookingController.getBookingById(userId, bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }

    @Test
    void getBookingsByUser_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        String state = "ALL";
        List<BookingFullDto> expectedBookingFullDto = List.of(new BookingFullDto());
        when(bookingService.getByUserIdAndState(userId, BookingState.from(state).get()))
                .thenReturn(expectedBookingFullDto);

        ResponseEntity<List<BookingFullDto>> response = bookingController.getBookingsByUser(userId, state);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingFullDto, response.getBody());
    }

    @Test
    void getBookingsByItem_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        String state = "All";
        List<BookingFullDto> expectedBookingFullDto = List.of(new BookingFullDto());
        when(bookingService.getAllByUserId(userId, from, size)).thenReturn(expectedBookingFullDto);

        ResponseEntity<List<BookingFullDto>> response = bookingController.getAll(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingFullDto, response.getBody());
    }
    @Test
    void getAll_whenInvoked_thenResponseStatusOkWithBookingDtoCollectionBody() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        String state = "ALL";
        List<BookingFullDto> expectedBookingFullDto = List.of(new BookingFullDto());
        when(bookingService.getAllBookingByItemsForUserId(userId, BookingState.ALL, from, size))
                .thenReturn(expectedBookingFullDto);

        ResponseEntity<List<BookingFullDto>> response = bookingController.getBookingsByItem(userId, state, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingFullDto, response.getBody());
    }

}