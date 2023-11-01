package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    private void addBooking() {
        ItemRequest itemRequest = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                1L,
                itemRequest);
        User user = new User(1L, "name", "emsil@emsil.com");
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        bookingRepository.save(Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> actualBooking = bookingRepository.findAllByBookerIdOrderByStartDesc(1L);

        assertEquals(1L, actualBooking.get(0).getId());
    }

    @Test
    void findAllByBookerIdOrderByEndDesc() {
        Page<Booking> actualBooking = bookingRepository
                .findAllByBookerIdOrderByEndDesc(1L, PageRequest.of(0, 1));

        assertEquals(1, actualBooking.get().count());
    }

    @Test
    void findAllByItemIdOrderByStartDesc() {
        List<Booking> actualBooking = bookingRepository.findAllByItemIdOrderByStartDesc(1L);

        assertEquals(1L, actualBooking.get(0).getId());
    }

    @Test
    void testFindAllByItemIdOrderByStartDesc() {
        Page<Booking> actualBooking = bookingRepository
                .findAllByItemIdOrderByStartDesc(1L, PageRequest.of(0, 1));

        assertEquals(1, actualBooking.get().count());
    }

    @AfterEach
    private void deleteBookings() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }
}