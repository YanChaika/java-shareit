package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    /*@BeforeEach
    public void addComment() {
        ItemRequest itemRequest = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                1L,
                itemRequest);
        User user = new User(1L, "name", "emsil@emsil.com");
        Comment comment = new Comment(1L, "text", item, user, LocalDateTime.now());
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        commentRepository.save(comment);
    }*/

    @Test
    void findAllByItemId() {
        ItemRequest itemRequest = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        Item item = new Item(
                1L,
                "item",
                "item description",
                true,
                1L,
                itemRequest);
        User user = new User(1L, "name", "emsil@emsil.com");
        Comment comment = new Comment(1L, "text", item, user, LocalDateTime.now());
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemRepository.save(item);
        commentRepository.save(comment);
        List<Comment> actualComments = commentRepository.findAllByItemId(1L);

        assertEquals(1L, actualComments.get(0).getId());

        commentRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }

    /*@AfterEach
    public void deleteComments() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }*/
}