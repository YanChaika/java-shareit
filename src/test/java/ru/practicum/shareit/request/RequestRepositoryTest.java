package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

@DataJpaTest
class RequestRepositoryTest {

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
        //User user = new User(1L, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("description");
        itemRequest.setRequesterId(1L);

        Assertions.assertNull(itemRequest.getId());
        //userRepository.save(user);
        requestRepository.save(itemRequest);
        Assertions.assertNotNull(itemRequest.getId());
    }

    /*@BeforeEach
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
    }*/

    /*@Test
    void findAllByRequesterIdOrderByCreatedDesc() {
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
        List<ItemRequest> actualItemRequest = requestRepository.findAllByRequesterIdOrderByCreatedDesc(1L);

        assertEquals(1L, actualItemRequest.get(0).getId());

        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }

    /*@AfterEach
    public void deleteBookings() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }*/
}