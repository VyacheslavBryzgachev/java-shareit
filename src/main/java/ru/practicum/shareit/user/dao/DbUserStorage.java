package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DbUserStorage {

    private final UserRepository userRepository;

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + id + " не найдено"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user, long id) {
        return userRepository.findById(id)
                .map(userUpd -> {
                    if (user.getName() != null) {
                        userUpd.setName(user.getName());
                    }
                    if (user.getEmail() != null) {
                        userUpd.setEmail(user.getEmail());
                    }
                    return userRepository.save(userUpd);
                })
                .orElseThrow(() ->
                        new UnknownIdException("Пользователя с таким id=" + id + " не найдено"));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}

