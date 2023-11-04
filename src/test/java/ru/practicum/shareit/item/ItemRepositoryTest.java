package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void verify() {
        //User user = new User(1L, "name", "emsil@emsil.com");
        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        //item.setAvailable(true);
        //item.setOwnerId(1L);
        //item.setRequest(new ItemRequest(1L, "description", user.getId(), LocalDateTime.now()));

        Assertions.assertNull(item.getId());
        //userRepository.save(user);
        itemRepository.save(item);
        Assertions.assertNotNull(item.getId());
    }

    /*@BeforeEach
    public void addItemRequest() {
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
    }

    /*@Test
    void findItemsByQuery() {
        List<Item> actualItem = itemRepository.findItemsByQuery("desc");

        assertEquals(1L, actualItem.get(0).getId());
    }

    @Test
    void findItemsByOwnerId() {
        List<Item> actualItem = itemRepository.findItemsByOwnerId(1L);

        assertEquals(1L, actualItem.get(0).getId());
    }*/

    /*@Test
    void findAllByRequestIdOrderByRequestCreatedDesc() {
        List<Item> actualItem = itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(1L);

        assertEquals(1L, actualItem.get(0).getId());
    }

    @AfterEach
    public void deleteItemRequests() {
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }*/
}