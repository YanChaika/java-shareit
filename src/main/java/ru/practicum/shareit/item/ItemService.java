package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    public ItemDto create(ItemDto itemDto, int userId);

    public ItemDto update(ItemUpdateDto itemDto, int userId, int itemId);

    public ItemDto findItemById(int id);

    public List<ItemDto> getAll();

    public List<ItemDto> getAllByUserId(int id);

    public List<ItemDto> search(String text);
}
