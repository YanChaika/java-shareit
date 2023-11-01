package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void addItemRequest() {
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
    @Test
    void findItemsByQuery() {
        List<Item> actualItem = itemRepository.findItemsByQuery("desc");

        assertEquals(1L, actualItem.get(0).getId());
    }

    @Test
    void findItemsByOwnerId() {
        List<Item> actualItem = itemRepository.findItemsByOwnerId(1L);

        assertEquals(1L, actualItem.get(0).getId());
    }

    @Test
    void findAllByRequestIdOrderByRequestCreatedDesc() {
        List<Item> actualItem = itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(1L);

        assertEquals(1L, actualItem.get(0).getId());
    }

    @AfterEach
    private void deleteItemRequests() {
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }
}