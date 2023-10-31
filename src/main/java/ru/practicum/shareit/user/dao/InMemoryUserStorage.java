package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final UserMapper userMapper = new UserMapper();
    private int idCounter = 0;


    public User getUserById(int userId) {
       if (users.get(userId) != null) {
           return users.get(userId);
       }
        throw new UnknownIdException("Пользователь с таким id=" + userId + " не найден");
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public UserDto createUser(User user) {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistException("Пользователь с таким email=" + user.getEmail() + " уже существует");
            }
        }
        setNextId();
        user.setId(idCounter);
        users.put(user.getId(), user);
        return userMapper.toUserDto(user);
    }

    public User updateUser(User user, int userId) {
        User userToUpd = getUserById(userId);
        for (User u : users.values()) {
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
        return userToUpd;
    }

    public void deleteUser(int userId) {
        users.remove(userId);
    }

    private void setNextId() {
        idCounter++;
    }
}
