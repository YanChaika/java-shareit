package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ValidatorFactory;

@JsonTest
@ContextConfiguration(classes = LocalValidatorFactoryBean.class)
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> jacksonTester;
    private BookingDto bookingDto;

    @Autowired
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void beforeEach() throws Exception {
        JsonContent<BookingDto> result = jacksonTester.write(bookingDto);
    }
}