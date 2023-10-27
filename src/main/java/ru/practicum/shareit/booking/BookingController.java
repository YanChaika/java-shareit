package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingFullDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody BookingDto booking) {
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingFullDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId,
                                 @RequestParam String approved) {
        return bookingService.update(approved, bookingId, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingFullDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @GetMapping(params = "state")
    public List<BookingFullDto> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getByUserIdAndState(userId, setStatus(state));
    }

    private BookingState setStatus(String state) {
        switch (state) {
            case "ALL":
                return BookingState.ALL;
            case "CURRENT":
                return BookingState.CURRENT;
            case "PAST":
                return BookingState.PAST;
            case "FUTURE":
                return BookingState.FUTURE;
            case "WAITING":
                return BookingState.WAITING;
            case "REJECTED":
                return BookingState.REJECTED;
            case "APPROVED":
                return BookingState.APPROVED;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @GetMapping("/owner")
    public List<BookingFullDto> getBookingsByItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false) String state,
                                                  @RequestParam(required = false) Long from,
                                                  @RequestParam(required = false) Long size) {
        if (state != null) {
            setStatus(state);
        } else {
            return bookingService.getAllBookingByItemsForUserId(userId, setStatus("ALL"), from, size);
        }
        return bookingService.getAllBookingByItemsForUserId(userId, setStatus(state), from, size);
    }

    @GetMapping
    public List<BookingFullDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(required = false) Long from,
                                       @RequestParam(required = false) Long size) {
        return bookingService.getAllByUserId(userId, from, size);
    }
}
