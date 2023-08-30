package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    int itemId = 1;

    @Override
    public ItemDto create(ItemDto itemDto, int userId) {
        log.info("Добавление вещи");
        userStorage.findUserById(userId);
        Item item = itemMapper.fromItemDto(itemDto, userId, itemId++);
        if (!item.isAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования");
        }
        return itemMapper.toFullItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(ItemUpdateDto itemDto, int userId, int itemId) {
        log.info("Обновление вещи");
        Item item;
        Item itemToChange = itemStorage.findItemById(itemId);
        if (itemDto.getAvailable() != null) {
            if (itemDto.getAvailable().equals("true")) {
                item = itemMapper.fromItemUpdateDto(itemDto, true, userId, itemId);
            } else {
                item = itemMapper.fromItemUpdateDto(itemDto, false, userId, itemId);
            }
        } else {
            item = itemMapper.fromItemUpdateDto(itemDto, itemToChange.isAvailable(), userId, itemId);
        }
        if (userId == itemToChange.getOwner()) {
            if (item.getName() != null) {
                itemToChange.setName(item.getName());}
            if (item.getDescription() != null) {
                itemToChange.setDescription(item.getDescription());
            }
            if (item.isAvailable()) {
                itemToChange.setAvailable(true);
            }
            if (!item.isAvailable()) {
                itemToChange.setAvailable(false);
            }
            itemStorage.update(itemToChange);
            return itemMapper.toFullItemDto(itemToChange);
        } else {
            throw new NotFoundException("Вы не являетесь владельцем вещи");
        }
    }

    @Override
    public ItemDto findItemById(int id) {
        log.info("Поиск вещи по id");
        return itemMapper.toFullItemDto(itemStorage.findItemById(id));
    }

    @Override
    public List<ItemDto> getAll() {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemStorage.getAll()) {
            itemsDto.add(itemMapper.toFullItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> getAllByUserId(int userId) {
        List<ItemDto> itemsToCheck = getAll();
        List<ItemDto> itemsByUser = new ArrayList<>();
        for (ItemDto itemDto : itemsToCheck) {
            if (itemDto.getOwner() == userId) {
                itemsByUser.add(itemDto);
            }
        }
        return itemsByUser;
    }

    @Override
    public List<ItemDto> search(String text) {
        log.info("Поиск вещи");
        List<ItemDto> toCheck = getAll();
        List<ItemDto> checked = new ArrayList<>();
        if (!text.equals("")) {
            for (ItemDto itemDto : toCheck) {
                if (itemDto.getName().toLowerCase().contains(text.toLowerCase())) {
                    if ((!checked.contains(itemDto)) && (itemDto.isAvailable())) {
                        checked.add(itemDto);
                    }
                }
                if (itemDto.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    if ((!checked.contains(itemDto)) && (itemDto.isAvailable())) {
                        checked.add(itemDto);
                    }
                }
            }
        }
        return checked;
    }
}
