package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.enums.Status;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> jsonTester;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void bookingDtoSerializationTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.MAX)
                .end(LocalDateTime.MAX)
                .build();
        String json = jsonTester.write(bookingDto).getJson();

        Assertions.assertEquals("{\"id\":1,\"bookerId\":1,\"itemId\":1,\"start\":\"+999999999-12-31T23:59:59.999999999\"," +
                "\"end\":\"+999999999-12-31T23:59:59.999999999\",\"status\":\"WAITING\"}", json);
    }

    @Test
    void bookingDtoDeserializationTest() throws Exception {
        String json = "{\"id\":1,\"bookerId\":1,\"itemId\":1,\"start\":\"+999999999-12-31T23:59:59.999999999\"," +
                "\"end\":\"+999999999-12-31T23:59:59.999999999\",\"status\":\"WAITING\"}";
        BookingDto bookingDto = jsonTester.parseObject(json);

        Assertions.assertEquals(1, bookingDto.getId());
        Assertions.assertEquals(1, bookingDto.getBookerId());
        Assertions.assertEquals(1, bookingDto.getItemId());
        Assertions.assertEquals(LocalDateTime.MAX, bookingDto.getStart());
        Assertions.assertEquals(LocalDateTime.MAX, bookingDto.getEnd());
        Assertions.assertEquals(Status.WAITING, bookingDto.getStatus());
    }

    @Test
    void bookingDtoValidationIfValidDtoTest() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.MAX)
                .end(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void bookingDtoValidationIfStartPastTest() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<BookingDto> violation = violations.iterator().next();
        Assertions.assertEquals("start", violation.getPropertyPath().toString());
    }

    @Test
    void bookingDtoValidationIfStarNullTest() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .end(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<BookingDto> violation = violations.iterator().next();
        Assertions.assertEquals("start", violation.getPropertyPath().toString());
    }

    @Test
    void bookingDtoValidationIfEndPastTest() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.MAX)
                .end(LocalDateTime.MIN)
                .build();

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<BookingDto> violation = violations.iterator().next();
        Assertions.assertEquals("end", violation.getPropertyPath().toString());
    }

    @Test
    void bookingDtoValidationIfStartNullTest() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<BookingDto> violation = violations.iterator().next();
        Assertions.assertEquals("end", violation.getPropertyPath().toString());
    }
}
