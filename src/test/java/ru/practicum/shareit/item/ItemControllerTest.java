package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemServiceImpl itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void createItem_whenInvoked_thenResponseStatusOkWithItemBody() {
        long userId = 0L;
        ItemDto expectedItemDto = new ItemDto();
        when(itemService.create(expectedItemDto, userId)).thenReturn(expectedItemDto);

        ResponseEntity<ItemDto> response = itemController.createItem(expectedItemDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemDto, response.getBody());
    }

    @Test
    void updateItem_whenInvoked_thenResponseStatusOkWithItemBody() {
        long userId = 0L;
        long itemId = 0L;
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        ItemDto expectedItemDto = new ItemDto();
        when(itemService.update(itemUpdateDto, userId, itemId)).thenReturn(expectedItemDto);

        ResponseEntity<ItemDto> response = itemController.updateItem(itemUpdateDto, userId, itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemDto, response.getBody());
    }

    @Test
    void getItemById_whenInvoked_thenResponseStatusOkWithItemBody() {
        long userId = 0L;
        long itemId = 0L;
        ItemDtoWithBookingDates expectedItem = new ItemDtoWithBookingDates();
        when(itemService.findItemById(itemId, userId)).thenReturn(expectedItem);

        ResponseEntity<ItemDtoWithBookingDates> response = itemController.getItemById(userId, itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItem, response.getBody());
    }

    @Test
    void getAllItemsByOwner_whenInvoked_thenResponseStatusOkWithItemsCollectionBody() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        List<ItemDtoWithBookingDates> expectedItems = List.of(new ItemDtoWithBookingDates());
        when(itemService.getAllByUserId(userId, from, size)).thenReturn(expectedItems);

        ResponseEntity<List<ItemDtoWithBookingDates>> response = itemController.getAllItemsByOwner(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
    }

    @Test
    void getItemByText_whenInvoked_thenResponseStatusOkWithItemsCollectionBody() {
        String text = "text";
        Long from = 1L;
        Long size = 1L;
        List<ItemDto> expectedItems = List.of(new ItemDto());
        when(itemService.search(text, from, size)).thenReturn(expectedItems);

        ResponseEntity<List<ItemDto>> response = itemController.getItemByText(text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
    }

    @Test
    void createComment_whenInvoked_thenResponseStatusOkWithCommentBody() {
        long userId = 0L;
        long itemId = 0L;
        CommentDto commentDto = new CommentDto(0L, "text");
        CommentFullDto expectedComment = CommentMapper.toCommentFullDto(CommentMapper.fromCommentDto(
                commentDto,
                new Item(),
                new User(),
                LocalDateTime.now())
        );
        when(itemService.createComment(commentDto, itemId, userId)).thenReturn(expectedComment);

        ResponseEntity<CommentFullDto> response = itemController.createComment(userId, itemId, commentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedComment, response.getBody());
    }
}