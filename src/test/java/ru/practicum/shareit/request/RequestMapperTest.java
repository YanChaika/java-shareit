package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @Test
    public void shouldMapEntityToDto() {
        LocalDateTime time = LocalDateTime.now();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(time);
        itemRequest.setRequesterId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setId(1L);

        ItemRequestWithTimeDto actualItemRequestDto = RequestMapper.toItemRequestWithTimeDto(itemRequest);

        assertThat(actualItemRequestDto).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void toItemRequestWithTimeDto_whenInvoked_thenReturn() {
        ItemRequest itemRequest = new ItemRequest();
        LocalDateTime time = LocalDateTime.now();
        itemRequest.setCreated(time);
        itemRequest.setRequesterId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setId(1L);
        ItemRequestWithTimeDto itemRequestWithTimeDto = new ItemRequestWithTimeDto();
        itemRequestWithTimeDto.setCreated(time.toString());
        itemRequestWithTimeDto.setDescription("desc");
        itemRequestWithTimeDto.setId(1L);
        ItemRequestWithTimeDto actualItemRequestWithTimeDto = RequestMapper.toItemRequestWithTimeDto(itemRequest);

        assertEquals(itemRequestWithTimeDto, actualItemRequestWithTimeDto);
        assertEquals(1L, actualItemRequestWithTimeDto.getId());
        assertEquals("desc", actualItemRequestWithTimeDto.getDescription());
        assertEquals(itemRequestWithTimeDto.getCreated(), actualItemRequestWithTimeDto.getCreated());
    }

    @Test
    void toItemRequestWithTimeDto() {
        ItemRequest itemRequest = new ItemRequest();
        LocalDateTime time = LocalDateTime.now();
        itemRequest.setCreated(time);
        itemRequest.setRequesterId(1L);
        ItemRequestWithTimeDto itemRequestWithTimeDto = new ItemRequestWithTimeDto();
        itemRequestWithTimeDto.setCreated(time.toString());
        ItemRequestWithTimeDto actualItemRequestWithTimeDto = RequestMapper.toItemRequestWithTimeDto(itemRequest);

        assertEquals(itemRequestWithTimeDto, actualItemRequestWithTimeDto);
    }
}