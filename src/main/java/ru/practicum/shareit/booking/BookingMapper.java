package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus()
        );
        return bookingDto;
    }

    public static Booking fromBookingDto(BookingDto bookingDto, User booker, Item item) {
        Booking booking = new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                bookingDto.getStatus()
        );
        return booking;
    }

    public static BookingFullDto toBookingFullDto(Booking booking, User user, Item item) {
        BookingFullDto bookingFullDto = new BookingFullDto(
                booking.getId(),
                booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                item,
                user,
                booking.getStatus()
        );
        return bookingFullDto;
    }

    public static List<BookingFullDto> foBookingsFullDto(List<Booking> bookings) {
        List<BookingFullDto> bookingsFullDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingsFullDto.add(BookingMapper.toBookingFullDto(booking, booking.getBooker(), booking.getItem()));
        }
        return bookingsFullDto;
    }
}
