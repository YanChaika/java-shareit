package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void addUsers() {
        userRepository.save(User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build());
    }

    @Test
    void findUserByEmail() {
         User actualUser = userRepository.findUserByEmail("email@email.com");

         assertEquals("email@email.com", actualUser.getEmail());
    }

    @AfterEach
    private void deleteUsers() {
        userRepository.deleteAll();
    }
}