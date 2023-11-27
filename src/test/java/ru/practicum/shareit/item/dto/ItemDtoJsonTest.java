package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> jsonTester;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void itemDtoSerializationTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(1)
                .name("Item 1")
                .description("Item1Desc")
                .available(true)
                .requestId(1)
                .build();

        String json = jsonTester.write(itemDto).getJson();

        Assertions.assertEquals(
                "{\"id\":1,\"owner\":1,\"name\":\"Item 1\",\"description\":\"Item1Desc\",\"available\":true," +
                        "\"lastBooking\":null,\"nextBooking\":null,\"comments\":null,\"requestId\":1}",
                json);
    }

    @Test
    public void itemDtoDeserializationTest() throws Exception {
        String json = "{\"id\":1,\"owner\":1,\"name\":\"Item 1\",\"description\":\"Item1Desc\",\"available\":true,\"requestId\":1}";

        ItemDto itemDto = jsonTester.parseObject(json);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals(1, itemDto.getOwner());
        Assertions.assertEquals("Item 1", itemDto.getName());
        Assertions.assertEquals("Item1Desc", itemDto.getDescription());
        Assertions.assertTrue(itemDto.getAvailable());
        Assertions.assertEquals(1, itemDto.getRequestId());
    }

    @Test
    public void itemDtoValidationIfValidDtoTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(1)
                .name("Item 1")
                .description("Item1Desc")
                .available(true)
                .requestId(1)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void itemDtoValidationIfBlankNameTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(1)
                .name("")
                .description("Item1Desc")
                .available(true)
                .requestId(1)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<ItemDto> violation = violations.iterator().next();
        Assertions.assertEquals("name", violation.getPropertyPath().toString());
    }


    @Test
    public void itemDtoValidationIfNullNameTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(1)
                .description("Item1Desc")
                .available(true)
                .requestId(1)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        Assertions.assertEquals(2, violations.size());
        ConstraintViolation<ItemDto> violation = violations.iterator().next();
        Assertions.assertEquals("name", violation.getPropertyPath().toString());
    }


    @Test
    public void itemDtoValidationIfNullDescriptionTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(1)
                .name("Item1")
                .available(true)
                .requestId(1)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<ItemDto> violation = violations.iterator().next();
        Assertions.assertEquals("description", violation.getPropertyPath().toString());
    }


    @Test
    public void itemDtoValidationIfNullAvailableTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(1)
                .name("Item1")
                .description("Item1Desc")
                .requestId(1)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<ItemDto> violation = violations.iterator().next();
        Assertions.assertEquals("available", violation.getPropertyPath().toString());
    }
}
