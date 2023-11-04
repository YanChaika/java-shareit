package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void getUserByIdOrThrow_whenUserFound_thenReturnUser() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserByIdOrThrow(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByIdOrThrow_whenUserNotFound_thenReturnNotFoundExceptionThrow() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserByIdOrThrow(userId));
    }

    @Test
    void create_whenUserEmailValid_thenReturnNullPointerExceptionThrow() {
        UserDto userDto = new UserDto();

        assertThrows(NullPointerException.class,
                () -> userService.create(userDto));
        verify(userRepository, never()).save(UserMapper.fromUserDto(userDto));
        verify(userRepository, times(0)).save(UserMapper.fromUserDto(userDto));
        verify(userRepository, atMost(5)).save(UserMapper.fromUserDto(userDto));
    }

    @Test
    void update_whenUserFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("email@email.com");

        User newUser = new User();
        newUser.setId(userId);
        newUser.setName("newName");
        newUser.setEmail("newEmail@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        User actualUser = UserMapper.fromUserDto(userService.update(UserMapper.toUserUpdateDto(newUser), userId));

        verify(userRepository).saveAndFlush(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("newName", savedUser.getName());
        assertEquals("newEmail@email.com", savedUser.getEmail());
    }

    @Test
    void update_whenUserNotFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("email@email.com");

        User newUser = new User();
        newUser.setId(userId);
        newUser.setName("newName");
        newUser.setEmail("newEmail@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(UserMapper.toUserUpdateDto(newUser), userId));
    }

    @Test
    void getAll_whenUserFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("email@email.com");

        User newUser = new User();
        newUser.setId(userId);
        newUser.setName("newName");
        newUser.setEmail("newEmail@email.com");
        when(userRepository.findAll()).thenReturn(List.of(oldUser));

        List<UserDto> actualUser = userService.getAll();

        assertEquals(UserMapper.mapToItemDto(List.of(oldUser)), actualUser);
    }

    @Test
    void create_whenUserFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("email@email.com");

        UserDto newUser = new UserDto();
        newUser.setId(userId);
        newUser.setName("newName");
        newUser.setEmail("newEmail@email.com");
        when(userRepository.save(any(User.class))).thenReturn(oldUser);

        UserDto actualUser = userService.create(newUser);

        assertEquals(oldUser.getName(), actualUser.getName());
    }

    @Test
    void create_whenUserNotFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("emailemail.com");
        UserDto newUser = new UserDto();
        newUser.setId(userId);
        newUser.setName("newName");
        newUser.setEmail("newEmailemail.com");

        assertThrows(ValidationException.class, () -> userService.create(newUser));
    }

    @Test
    void deleteById_whenUserFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("email@email.com");

        userRepository.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteById_whenUserNotFound_thenUpdate() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name");
        oldUser.setEmail("email@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteById(oldUser.getId()));
    }

}
