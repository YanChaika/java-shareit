package ru.practicum.shareit.item.dto;

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
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> jacksonTester;
    private CommentDto commentDto;

    @Autowired
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void beforeEach() {
        commentDto = new CommentDto(1L, "name");
    }

    @Test
    void testJsonUserDto() throws Exception {
        JsonContent<CommentDto> result = jacksonTester.write(commentDto);

    }

}