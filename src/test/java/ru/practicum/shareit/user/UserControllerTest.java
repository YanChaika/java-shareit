package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_whenInvoked_thenResponseStatusOkWithUsersDtoCollectionBody() {
        List<UserDto> expectedUsersDto = List.of(new UserDto());
        when(userService.getAll()).thenReturn(expectedUsersDto);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsersDto, response.getBody());
    }

    @Test
    void getUserById_whenInvoked_thenResponseStatusOkWithUsersDtoBody() {
        long userId = 0L;
        User expectedUsers = new User();
        when(userService.getUserByIdOrThrow(userId)).thenReturn(expectedUsers);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
    }

    @Test
    void createUser_whenInvoked_thenResponseStatusOkWithUsersDtoBody() {
        UserDto expectedUsersDto = new UserDto();
        when(userService.create(expectedUsersDto)).thenReturn(expectedUsersDto);

        ResponseEntity<UserDto> response = userController.createUser(expectedUsersDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsersDto, response.getBody());
    }

    @Test
    void putUser_whenInvoked_thenResponseStatusOkWithUsersDtoBody() {
        long userId = 0L;
        UserDto expectedUser = new UserDto();
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        when(userService.update(userUpdateDto, userId)).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.putUser(userUpdateDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    void deleteUser_whenInvoked_thenResponseStatusOkWithUsersDtoBody() {
        long userId = 0L;
        UserDto expectedUsersDto = new UserDto(0L, "name", "email@email.com");
        when(userService.create(expectedUsersDto)).thenReturn(expectedUsersDto);
        userService.deleteById(userId);

        ResponseEntity<UserDto> response = userController.createUser(expectedUsersDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsersDto, response.getBody());
    }
}
