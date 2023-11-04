package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    private EntityManager em;

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
        when(bookingRepository.findAllByItemIdOrderByStartDesc(
                item.getId(),
                PageRequest.of(from.intValue(), size.intValue())
        )).thenReturn(page);

        List<BookingFullDto> actualBookings = bookingService
                .getAllBookingByItemsForUserId(userId, BookingState.PAST, from, size);

        assertEquals(List.of(bookingFullDto), actualBookings);
    }

    @Test
    void getAllBookingByItemsForUserId_whenBookingWasNotFound_thenReturnBookings() {
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
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        PageImpl<Booking> page = new PageImpl<>(List.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findItemsByOwnerId(userId)).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(
                item.getId(),
                PageRequest.of(from.intValue(), size.intValue())
        )).thenReturn(page);

        List<BookingFullDto> actualBookings = bookingService
                .getAllBookingByItemsForUserId(userId, BookingState.CURRENT, from, size);

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
                .getAllBookingByItemsForUserId(userId, BookingState.CURRENT, from, size));
    }

    @Test
    void create_whenNotInvoked_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService
                .create(bookingDto, userId));
    }

    @Test
    void create_whenInvoked_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingFullDto actualBooking  = bookingService.create(bookingDto, userId);

        assertEquals(BookingMapper.toBookingFullDto(booking, user, item), actualBooking);
    }

    @Test
    void create_whenNotInvokedBookingTime_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = timeStart;
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);

        assertThrows(ValidationException.class, () -> bookingService.create(bookingDto, userId));
    }

    @Test
    void create_whenUserNotFound_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now();
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto, userId));
    }

    @Test
    void create_whenItemNotFound_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now();
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto, userId));
    }

    @Test
    void create_whenOwnerNotFound_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now();
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto, userId));
    }

    @Test
    void update_whenInvoked_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);


       BookingFullDto actualBookings = bookingService.update("true", booking.getId(), userId);

        assertEquals(booking.getItem(), actualBookings.getItem());
    }

    @Test
    void update_whenInvokedWrong_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.update("true", booking.getId(), userId));
    }

    @Test
    void update_whenNotInvokedWasApproved_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.update("true", booking.getId(), userId));
    }

    @Test
    void update_whenInvoked_thenReturnRejectedBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);


        BookingFullDto actualBookings = bookingService.update("false", booking.getId(), userId);

        assertEquals(booking.getItem(), actualBookings.getItem());
    }


    @Test
    void get_whenInvoked_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingFullDto actualBookings = bookingService.get(userId, booking.getId());

        assertEquals(booking.getItem(), actualBookings.getItem());
    }

    @Test
    void get_whenNotInvoked_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.get(userId, booking.getId()));
    }

    @Test
    void get_whenNotInvokedBookingNotFound_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.get(userId, booking.getId()));;
    }

    @Test
    void get_whenUserNotInvoked_thenReturnException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        User user2 = new User(2L, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                2L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(BookingStatus.WAITING);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.get(userId, booking.getId()));;
    }



    @Test
    void getByUserIdAndState_whenInvoked_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));

        List<BookingFullDto> actualBookings = bookingService.getByUserIdAndState(userId, BookingState.ALL);

        assertEquals(1, actualBookings.size());
    }

    @Test
    void getByUserIdAndState_whenNotInvoked_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now().plusDays(1L);
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(2L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));

        List<BookingFullDto> actualBookings = bookingService.getByUserIdAndState(userId, BookingState.FUTURE);

        assertEquals(1, actualBookings.size());
    }

    @Test
    void getByUserIdAndState_whenInvokedAndState_thenReturnBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now().plusDays(1L);
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(2L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                false,
                1L,
                itemRequest);
        Booking booking = new Booking();
        booking.setStart(timeStart);
        booking.setEnd(timeEnd);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(timeStart);
        bookingDto.setEnd(timeEnd);
        Booking bookingWithId = booking;
        bookingWithId.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));

        List<BookingFullDto> actualBookings = bookingService.getByUserIdAndState(userId, BookingState.WAITING);

        assertEquals(1, actualBookings.size());
    }

    @Test
    void getByUserIdAndState_whenUserNotFound_thenReturnException() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getByUserIdAndState(userId, BookingState.ALL));
    }

    @Test
    void getAllBookingByItemsForUserId_whenUserNotFound_thenReturnException() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingByItemsForUserId(userId, BookingState.ALL, 0L, 1L));
    }

    @Test
    void update_whenUserNotFound_thenReturnException() {
        long userId = 1L;

        assertThrows(NotFoundException.class,
                () -> bookingService.update("true", 1L, 1L));
    }
}