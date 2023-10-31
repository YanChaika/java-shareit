package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;

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
    void getAllItemsByOwner() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        List<ItemDtoWithBookingDates> expectedItems = List.of(new ItemDtoWithBookingDates());
        when(itemService.getAllByUserId(userId, from, size)).thenReturn(expectedItems);

        ResponseEntity<List<ItemDtoWithBookingDates>> response = itemController.getAllItemsByOwner(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
    }
}