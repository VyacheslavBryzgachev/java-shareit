package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.DbUserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper = new UserMapper();
    private final DbUserStorage dbUserStorage;

    @Override
    public UserDto getUserById(long userId) {
        return userMapper.toUserDto(dbUserStorage.getUserById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = dbUserStorage.getAllUsers();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(userMapper.toUserDto(user));
        }
        return userDtoList;
    }

    @Override
    public User createUser(UserDto userDto) {
        return dbUserStorage.createUser(userMapper.toUser(userDto));
    }


    @Override
    public User updateUser(User user, long userId) {
        return dbUserStorage.updateUser(user, userId);
    }

    @Override
    public void deleteUser(long userId) {
        dbUserStorage.deleteUser(userId);
    }
}

