package ru.practicum.shareit.user.dto;

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
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> jsonTester;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void userDtoSerializationTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("User1@mail.ru")
                .build();

        String json = jsonTester.write(userDto).getJson();

        Assertions.assertEquals("{\"id\":1,\"name\":\"User1\",\"email\":\"User1@mail.ru\"}", json);
    }

    @Test
    public void userDtoDeserializationTest() throws Exception {
        String jsonString = "{\"id\":1,\"name\":\"User1\",\"email\":\"User1@mail.ru\"}";

        UserDto userDto = jsonTester.parseObject(jsonString);

        Assertions.assertEquals(1, userDto.getId());
        Assertions.assertEquals("User1", userDto.getName());
        Assertions.assertEquals("User1@mail.ru", userDto.getEmail());
    }

    @Test
    public void userDtoValidationIfValidDtoTest() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("User1@mail.ru")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void userDtoValidationIfEmailNullTest() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<UserDto> violation = violations.iterator().next();
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    public void userDtoValidationIfEmailNoValidTest() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("User1")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<UserDto> violation = violations.iterator().next();
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }
}
