package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;

@JsonTest
@ContextConfiguration(classes = LocalValidatorFactoryBean.class)
class BookingFullDtoTest {
    @Autowired
    private JacksonTester<BookingFullDto> jacksonTester;
    private BookingFullDto bookingFullDto;

    @Autowired
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void beforeEach() {
        bookingFullDto = new BookingFullDto(
                1L,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                new Item(),
                new User(),
                BookingStatus.WAITING
        );
    }

    @Test
    void testJsonUserDto() throws Exception {
        JsonContent<BookingFullDto> result = jacksonTester.write(bookingFullDto);

    }

}