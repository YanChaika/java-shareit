package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidatorFactory;

@JsonTest
@ContextConfiguration(classes = LocalValidatorFactoryBean.class)
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;
    private ItemRequestDto itemRequestDto;

    @Autowired
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void beforeEach() {
        itemRequestDto = new ItemRequestDto(1L, "description");
    }

    @Test
    void testJsonUserDto() throws Exception {
        JsonContent<ItemRequestDto> result = jacksonTester.write(itemRequestDto);

    }
}