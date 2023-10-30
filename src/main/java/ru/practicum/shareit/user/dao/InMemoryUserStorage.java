package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 0;


    public User getUserById(int userId) {
        for (User user : users.values()) {
            if (user.getId() == userId) {
                return user;
            }
        }
        throw new UnknownIdException("Пользователь с таким id=" + userId + " не найден");
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistException("Пользователь с таким email=" + user.getEmail() + " уже существует");
            }
        }
        setNextId();
        user.setId(idCounter);
        users.put(user.getId(), user);
        return user;
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
