package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;


@SpringBootTest
@Sql(scripts = {"classpath:schemaTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:dataTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void getUserByIdReturnValidUserIfValidIdArgument() {
        UserDto expectedUser = new UserDto(1, "User1", "User1@mail.ru");
        UserDto actualUser = userService.getUserById(1);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByIdThrowExceptionIfNotValidArgument() {
        UnknownIdException exception = Assertions.assertThrows(UnknownIdException.class,
                () -> userService.getUserById(99));
        Assertions.assertEquals(exception.getMessage(),
                "Пользователя с таким id=99 не найдено");
    }

    @Test
    void getAllUsersReturnValidUserList() {
        List<UserDto> expected = List.of(UserDto.builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build(),
                UserDto.builder()
                        .id(2)
                        .name("User2")
                        .email("User2@mail.ru")
                        .build());
        List<UserDto> users = userService.getAllUsers();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(expected.get(0), users.get(0));
        Assertions.assertEquals(expected.get(1), users.get(1));
    }

    @Test
    void createUserReturnValidUserIfValidArguments() {
        User expected = User.builder()
                .id(3)
                .name("User3")
                .email("User3@mail.ru")
                .build();
        User actual = userService.createUser(UserDto.builder()
                .name("User3")
                .email("User3@mail.ru")
                .build());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateUserReturnUpdateUserIfValidUserId() {
        User userUpd = User.builder()
                .id(2)
                .name("User2Upd")
                .email("User2Upd@mail.ru")
                .build();
        User user = userService.updateUser(userUpd, 2);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userUpd, user);
    }

    @Test
    void updateUserThrowExceptionIfNotValidUserId() {
        User userUpd = User.builder()
                .id(2)
                .name("User2Upd")
                .email("User2Upd@mail.ru")
                .build();
        UnknownIdException exception = Assertions.assertThrows(UnknownIdException.class,
                () -> userService.updateUser(userUpd, 99));
        Assertions.assertEquals(exception.getMessage(),
                "Пользователя с таким id=99 не найдено");
    }
}
