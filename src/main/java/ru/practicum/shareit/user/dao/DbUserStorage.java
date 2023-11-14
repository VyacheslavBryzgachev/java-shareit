package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DbUserStorage {

    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + id + " не найдено"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDto createUser(User user) {
        return userMapper.toUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UserDto userDto, long id) {
        return userRepository.findById(id)
                .map(userUpd -> {
                    if (userDto.getName() != null) {
                        userUpd.setName(userDto.getName());
                    }
                    if (userDto.getEmail() != null) {
                        userUpd.setEmail(userDto.getEmail());
                    }
                    return userMapper.toUserDto(userRepository.save(userUpd));
                })
                .orElseThrow(() ->
                        new UnknownIdException("Пользователя с таким id=" + id + " не найдено"));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}

