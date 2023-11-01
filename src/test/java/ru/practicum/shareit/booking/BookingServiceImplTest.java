package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getAllByUserId_whenBookingFound_thenReturnBookings() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        User user = new User();
        Item item = new Item();
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING
        );
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdOrderByEndDesc(
                userId,
                PageRequest.of(from.intValue(), size.intValue())
        )).thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingFullDto> actualBookings = bookingService.getAllByUserId(userId, from, size);

        assertEquals(List.of(bookingFullDto), actualBookings);
    }

    @Test
    void getAllByUserId_whenBookingNotFound_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getAllByUserId(userId, from, size));
    }

    @Test
    void getAllBookingByItemsForUserId_whenBookingNotFound_thenReturnBookings() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        PageImpl<Booking> page = new PageImpl<>(List.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findItemsByOwnerId(userId)).thenReturn(List.of(item));
        //when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(
                item.getId(),
                PageRequest.of(from.intValue(), size.intValue())
        )).thenReturn(page);

        List<BookingFullDto> actualBookings = bookingService
                .getAllBookingByItemsForUserId(userId, BookingState.ALL, from, size);

        assertEquals(List.of(bookingFullDto), actualBookings);
    }

    @Test
    void getAllBookingByItemsForUserId_whenFromAndSizeZero_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findItemsByOwnerId(userId)).thenReturn(List.of(item));

        assertThrows(ValidationException.class, () -> bookingService
                .getAllBookingByItemsForUserId(userId, BookingState.ALL, from, size));
    }
}