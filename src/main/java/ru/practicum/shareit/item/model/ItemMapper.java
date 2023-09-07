package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Component
public class ItemMapper {

    public ItemDto toFullItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public ItemDto toFullItemUpdateDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public Item fromItemDto(ItemDto itemDto, int ownerId, int itemId) {
        return new Item(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                ownerId,
                null
        );
    }

    public Item fromItemUpdateDto(ItemUpdateDto itemDto, boolean isTrue, int ownerId, int itemId) {
        return new Item(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                isTrue,
                ownerId,
                null
        );
    }
}
