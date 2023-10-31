package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.InMemoryUserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserMapper userMapper = new UserMapper();

    @Override
    public UserDto getUserById(int userId) {
        return userMapper.toUserDto(inMemoryUserStorage.getUserById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = inMemoryUserStorage.getAllUsers();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(userMapper.toUserDto(user));
        }
        return userDtoList;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return inMemoryUserStorage.createUser(userMapper.toUser(userDto));
    }

    @Override
    public UserDto updateUser(User user, int userId) {
        return userMapper.toUserDto(inMemoryUserStorage.updateUser(user, userId));
    }

    @Override
    public void deleteUser(int userId) {
        inMemoryUserStorage.deleteUser(userId);
    }

}

