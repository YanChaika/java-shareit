package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;

    private User user;
    private ItemRequest itemRequest;
    private Item item;
    private Booking booking;

    /*@BeforeEach
    public void addBooking() {
        user = new User(1L, "name", "emsil@emsil.com");
        itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        requestRepository.save(itemRequest);
        booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        bookingRepository.save(booking);
    }*/

    @Test
    public void findAllByBookerIdOrderByStartDesc() {
        user = new User(1L, "name", "emsil@emsil.com");
        itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        requestRepository.save(itemRequest);
        booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        bookingRepository.save(booking);
        List<Booking> actualBooking = bookingRepository.findAllByBookerIdOrderByStartDesc(user.getId());

        assertEquals(1L, actualBooking.get(0).getId());

        bookingRepository.deleteById(booking.getId());
        itemRepository.deleteById(item.getId());
        requestRepository.deleteById(itemRequest.getId());
        userRepository.deleteById(user.getId());
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void findAllByBookerIdOrderByEndDesc() {
        user = new User(1L, "name", "emsil@emsil.com");
        itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        requestRepository.save(itemRequest);
        booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        bookingRepository.save(booking);
        Page<Booking> actualBooking = bookingRepository
                .findAllByBookerIdOrderByEndDesc(user.getId(), PageRequest.of(0, 1));

        assertEquals(1, actualBooking.get().count());

        bookingRepository.deleteById(booking.getId());
        itemRepository.deleteById(item.getId());
        requestRepository.deleteById(itemRequest.getId());
        userRepository.deleteById(user.getId());
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void findAllByItemIdOrderByStartDesc() {
        user = new User(1L, "name", "emsil@emsil.com");
        itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        requestRepository.save(itemRequest);
        booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        bookingRepository.save(booking);
        List<Booking> actualBooking = bookingRepository.findAllByItemIdOrderByStartDesc(item.getId());

        assertEquals(1L, actualBooking.get(0).getId());

        bookingRepository.deleteById(booking.getId());
        itemRepository.deleteById(item.getId());
        requestRepository.deleteById(itemRequest.getId());
        userRepository.deleteById(user.getId());
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void testFindAllByItemIdOrderByStartDesc() {
        user = new User(1L, "name", "emsil@emsil.com");
        itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        item = new Item(
                1L,
                "item",
                "item description",
                true,
                user.getId(),
                itemRequest);
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        requestRepository.save(itemRequest);
        booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.WAITING);
        bookingRepository.save(booking);
        Page<Booking> actualBooking = bookingRepository
                .findAllByItemIdOrderByStartDesc(item.getId(), PageRequest.of(0, 1));

        assertEquals(1, actualBooking.get().count());

        bookingRepository.deleteById(booking.getId());
        itemRepository.deleteById(item.getId());
        requestRepository.deleteById(itemRequest.getId());
        userRepository.deleteById(user.getId());
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    /*@AfterEach
    public void deleteBookings() {
        bookingRepository.deleteById(booking.getId());
        itemRepository.deleteById(item.getId());
        requestRepository.deleteById(itemRequest.getId());
        userRepository.deleteById(user.getId());
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }*/
}