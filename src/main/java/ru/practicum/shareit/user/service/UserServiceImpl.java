package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();
    private final UserMapper userMapper = new UserMapper();
    private int idCounter = 0;

    @Override
    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
       throw new UnknownIdException("Пользователь с таким id=" + userId + " не найден");
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User createUser(User user) {
        for (User u : users) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistException("Пользователь с таким email=" + user.getEmail() + " уже существует");
            }
        }
        setNextId();
        user.setId(idCounter);
        users.add(user);
        return user;
    }

    @Override
    public UserDto updateUser(UserDto user, int userId) {
        User userToUpd = getUserById(userId);
        for (User u : users) {
            if (u.getEmail().equals(user.getEmail()) && u.getId() != userToUpd.getId()) {
                throw new EmailAlreadyExistException("Пользователь с таким email=" + user.getEmail() + " уже существует");
            }
        }
        if (user.getEmail() != null) {
            userToUpd.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userToUpd.setName(user.getName());
        }
        return userMapper.toUserDto(userToUpd);
    }

    @Override
    public void deleteUser(int userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    private void setNextId() {
        idCounter++;
    }
}

