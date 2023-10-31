package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    static ItemRequest fromItemRequestDto(ItemRequestDto itemRequestDto, Long userId, LocalDateTime time) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                userId,
                time
                );
    }

    static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription()
        );
    }

    static List<ItemRequestDto> toItemRequestsDto(List<ItemRequest> itemRequest) {
        List<ItemRequestDto> itemRequestsDto = new ArrayList<>();
        for (ItemRequest request : itemRequest) {
            itemRequestsDto.add(toItemRequestDto(request));
        }
        return itemRequestsDto;
    }

    static ItemRequestFullDto toItemRequestFullDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestFullDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated().toString(),
                items
        );
    }

    static ItemRequestWithTimeDto toItemRequestWithTimeDto(ItemRequest itemRequest) {
        return new ItemRequestWithTimeDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated().toString()
        );
    }
}
