package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;

    @BeforeEach
    public void addBooking() {
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
    void findAllByRequesterIdOrderByCreatedDesc() {
        List<ItemRequest> actualItemRequest = requestRepository.findAllByRequesterIdOrderByCreatedDesc(1L);

        assertEquals(1L, actualItemRequest.get(0).getId());
    }

    @AfterEach
    public void deleteBookings() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }
}