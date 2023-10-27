package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ValidatorFactory;

@JsonTest
@ContextConfiguration(classes = LocalValidatorFactoryBean.class)
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;
    private UserDto userDto;

    @Autowired
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1L, "name", "email@email.com");
    }

    @Test
    void testJsonUserDto() throws Exception {
        JsonContent<UserDto> result = jacksonTester.write(userDto);

    }


}