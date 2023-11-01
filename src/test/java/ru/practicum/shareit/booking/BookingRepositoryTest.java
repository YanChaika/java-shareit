package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void verify() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setItem(new Item());
        booking.setBooker(new User());
        booking.setStatus(BookingStatus.WAITING);

        Assertions.assertNull(booking.getId());
        em.persist(booking);
        Assertions.assertNotNull(booking.getId());
    }

    //private User user;
    //private ItemRequest itemRequest;
    //private Item item;
    //private Booking booking;

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
    }

    /*@Test
    public void findAllByBookerIdOrderByStartDesc() {
        List<Booking> actualBooking = bookingRepository.findAllByBookerIdOrderByStartDesc(user.getId());

        assertEquals(1L, actualBooking.get(0).getId());
    }

    @Test
    public void findAllByBookerIdOrderByEndDesc() {
        Page<Booking> actualBooking = bookingRepository
                .findAllByBookerIdOrderByEndDesc(user.getId(), PageRequest.of(0, 1));

        assertEquals(1, actualBooking.get().count());
    }

    @Test
    public void findAllByItemIdOrderByStartDesc() {
        List<Booking> actualBooking = bookingRepository.findAllByItemIdOrderByStartDesc(item.getId());

        assertEquals(1L, actualBooking.get(0).getId());
    }*/

    /*@Test
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

        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    /*@AfterEach
    public void deleteBookings() {
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }*/
}