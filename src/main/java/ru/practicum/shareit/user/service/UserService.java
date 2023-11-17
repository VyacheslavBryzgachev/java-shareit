package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto getUserById(long userId);

    List<UserDto> getAllUsers();

    User createUser(UserDto userDto);

    User updateUser(User user, long userId);

    void deleteUser(long userId);
}
