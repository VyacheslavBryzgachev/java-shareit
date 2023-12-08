package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> jsonTester;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void commentDtoSerializationTest() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("Комментарий")
                .authorName("Автор")
                .item(Item.builder()
                        .id(1)
                        .owner(User.builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item 1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(1)
                        .build())
                .build();

        String json = jsonTester.write(commentDto).getJson();

        Assertions.assertEquals(
                "{\"id\":1,\"text\":\"Комментарий\",\"authorName\":\"Автор\",\"item\":{\"id\":1,\"owner\"" +
                        ":{\"id\":1,\"name\":\"User1\",\"email\":\"User1@mail.ru\"},\"name\"" +
                        ":\"Item 1\",\"description\":\"Item1Desc\",\"available\":true,\"requestId\":1}}",
                json);
    }

    @Test
    void commentDtoDeserializationTest() throws Exception {
        String json = "{\"id\":1,\"text\":\"Комментарий\",\"authorName\":\"Автор\",\"item\":{\"id\":1,\"owner\"" +
                ":{\"id\":1,\"name\":\"User1\",\"email\":\"User1@mail.ru\"},\"name\"" +
                ":\"Item 1\",\"description\":\"Item1Desc\",\"available\":true,\"requestId\":1}}";
        Item item = Item.builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .name("Item 1")
                .description("Item1Desc")
                .available(true)
                .requestId(1)
                .build();

        CommentDto commentDto = jsonTester.parseObject(json);

        Assertions.assertEquals(1, commentDto.getId());
        Assertions.assertEquals("Комментарий", commentDto.getText());
        Assertions.assertEquals("Автор", commentDto.getAuthorName());
        Assertions.assertEquals(item, commentDto.getItem());
    }

    @Test
    void commentDtoValidationIfValidDtoTest() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("Комментарий")
                .authorName("Автор")
                .item(Item.builder()
                        .id(1)
                        .owner(User.builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item 1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(1)
                        .build())
                .build();

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void commentDtoValidationIfTextBlankTest() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("")
                .authorName("Автор")
                .item(Item.builder()
                        .id(1)
                        .owner(User.builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item 1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(1)
                        .build())
                .build();

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<CommentDto> violation = violations.iterator().next();
        Assertions.assertEquals("text", violation.getPropertyPath().toString());
    }

    @Test
    void commentDtoValidationIfTextNullTest() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .authorName("Автор")
                .item(Item.builder()
                        .id(1)
                        .owner(User.builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item 1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(1)
                        .build())
                .build();

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        Assertions.assertEquals(2, violations.size());
        ConstraintViolation<CommentDto> violation = violations.iterator().next();
        Assertions.assertEquals("text", violation.getPropertyPath().toString());
    }
}
