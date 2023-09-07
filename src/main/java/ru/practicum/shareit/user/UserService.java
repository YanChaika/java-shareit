package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AlreadyPresentException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;
    private int id = 1;

    public List<UserDto> getAll() {
        log.info("Получение всех пользователей");
        List<User> users = userStorage.getAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(userMapper.toUserDto(user));
        }
        return usersDto;
    }

    public UserDto create(UserDto userDto) {
        log.info("Добавление пользователя");
        User user = userMapper.fromUserDto(userDto);
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("Email некорректный");
        } else {
            try {
            if (userStorage.findUserByEmail(user.getEmail()) != null) {
                throw new AlreadyPresentException("Пользователь c email " + user.getEmail() + " уже существует");
            }
            } catch (NotFoundException e) {
                user.setId(id++);
                userStorage.create(user);
            }
            return userMapper.toUserDto(user);
        }
    }

    public UserDto update(UserUpdateDto userUpdateDto, Integer userId) {
        log.info("Обновление пользователя");
        User user = userStorage.findUserById(userId);
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            try {
                if (userStorage.findUserByEmail(userUpdateDto.getEmail()) != null) {
                    User userToCheck = userStorage.findUserByEmail(userUpdateDto.getEmail());
                    if (userToCheck.getId() != userId) {
                        throw new AlreadyPresentException("Пользователь c email " + userUpdateDto.getEmail()
                                + " уже существует");
                    }
                }
            } catch (NotFoundException e) {
                user.setEmail(userUpdateDto.getEmail());
            }
        }
        userStorage.update(user);
        return userMapper.toUserDto(user);

    }

    public void delete(Integer id) {
        log.info("Удаление пользователя");
        userStorage.delete(id);
    }

    public User getUserById(Integer userId) {
        log.info("Поиск пользователя по id");
        return userStorage.findUserById(userId);
    }
}
