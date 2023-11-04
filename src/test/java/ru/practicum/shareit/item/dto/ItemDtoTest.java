package ru.practicum.shareit.item.dto;

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
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> jacksonTester;
    private ItemDto itemDto;

    @Autowired
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto(1L, "name", "description", true, 1L, 1L);
    }

    @Test
    void testJsonUserDto() throws Exception {
        JsonContent<ItemDto> result = jacksonTester.write(itemDto);

    }

}