package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jsonTester;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void itemRequestDtoSerializationTest() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .requester(1)
                .description("Описание")
                .created(LocalDateTime.MAX)
                .build();

        String json = jsonTester.write(itemRequestDto).getJson();

        Assertions.assertEquals("{\"id\":1,\"description\":\"Описание\",\"requester\":1," +
                        "\"created\":\"+999999999-12-31T23:59:59.999999999\",\"items\":null}"
                , json);
    }

    @Test
    void itemRequestDeserializationTest() throws Exception {
        String json = "{\"id\":1,\"description\":\"Описание\",\"requester\":1," +
                "\"created\":\"+999999999-12-31T23:59:59.999999999\",\"items\":null}";
        ItemRequestDto itemRequestDto = jsonTester.parseObject(json);

        Assertions.assertEquals(1, itemRequestDto.getId());
        Assertions.assertEquals(1, itemRequestDto.getRequester());
        Assertions.assertEquals("Описание", itemRequestDto.getDescription());
        Assertions.assertEquals(LocalDateTime.MAX, itemRequestDto.getCreated());
        Assertions.assertNull(itemRequestDto.getItems());
    }

    @Test
    void itemRequestValidationIfValidDtoTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .requester(1)
                .description("Описание")
                .created(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void itemRequestValidationIfDescriptionBlankTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .requester(1)
                .description("")
                .created(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();
        Assertions.assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    void itemRequestValidationIfDescriptionNullTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .requester(1)
                .created(LocalDateTime.MAX)
                .build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);

        Assertions.assertEquals(2, violations.size());
        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();
        Assertions.assertEquals("description", violation.getPropertyPath().toString());
    }
}
