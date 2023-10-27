package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyPresentException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class UserInMemoryStorage implements UserStorage {

    private Map<Long, User> users = new HashMap();

    @Override
    public List<User> getAll() {
        if (users.isEmpty()) {
            throw new NotFoundException("Список пользователей пуст");
        }
        log.info("Список пользователей предоставлен");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            throw new AlreadyPresentException("Пользователь с id " + user.getId() + " уже существует");
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        findUserById(user.getId());
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с id " + user.getId() + " обновлён");
        return user;
    }

    @Override
    public void delete(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        users.remove(id);
        log.info("Пользователь с id " + id + " удален");
    }

    @Override
    public User findUserByEmail(String email) {
        if (users.isEmpty()) {
            throw new NotFoundException("Список пользователей пуст");
        }
        for (Long aLong : users.keySet()) {
            User userToCheck = users.get(aLong);
            if (userToCheck.getEmail().equals(email)) {
                return userToCheck;
            }
        }
        throw new NotFoundException("Пользователь с email " + email + " не найден");
    }

    @Override
    public User findUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }
}
