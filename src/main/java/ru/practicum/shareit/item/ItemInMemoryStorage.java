package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyPresentException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemInMemoryStorage implements ItemStorage {

    private Map<Integer, Item> items = new HashMap();

    @Override
    public Item create(Item item) {
        if (items.containsKey(item.getId())) {
            throw new AlreadyPresentException("Вещь с id " + item.getId() + " уже существует");
        }
        items.put(item.getId(), item);
        log.info("Вещь добавлена");
        return item;
    }

    @Override
    public Item findItemById(int id) {
        log.info("Поиск вещи");
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new NotFoundException("Вещь с id " + id + " не найденa");
        }

    }

    @Override
    public Item update(Item item) {
        log.info("Обновление вещи");
        items.remove(item.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAll() {
        if (items.isEmpty()) {
            throw new NotFoundException("Спиwсок вещей пуст");
        }
        log.info("Список пользователей предоставлен");
        return items.values().parallelStream().collect(Collectors.toList());
    }
}
