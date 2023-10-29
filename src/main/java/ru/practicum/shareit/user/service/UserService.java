package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUserById(int userId);

    List<User> getAllUsers();

    User createUser(User user);

    UserDto updateUser(UserDto userDto, int userId);

    void deleteUser(int userId);
}
