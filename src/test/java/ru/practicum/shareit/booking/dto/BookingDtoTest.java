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

import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;

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
        bookingDto = new BookingDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                1L,
                BookingStatus.WAITING
        );
    }

    @Test
    void testJsonBookingDto() throws Exception {
        JsonContent<BookingDto> result = jacksonTester.write(bookingDto);

    }
}