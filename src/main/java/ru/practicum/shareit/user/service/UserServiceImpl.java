package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserListMapper;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.dao.UserStorageImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorageImpl userStorage;
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;

    @Override
    public UserDto createUser(UserDto user) {
        User saveUser = userStorage.createUser(userMapper.toUserModel(user));
        log.info("Пользователь создан");
        return userMapper.toUserDto(saveUser);
    }

    @Override
    public UserDto updateUser(int id, UserDto user) {
        UserDto noUpdateUser = get(id);
        user.setId(id);
        User updateUser = userStorage.updateUser(userMapper.toUserModel(user));
        log.info("Пользователь обновлен");
        return userMapper.toUserDto(updateUser);
    }

    @Override
    public UserDto get(int id) {
        Optional<User> user = userStorage.get(id);
        log.info(String.format("Пользователь по id = %s получен", id));
        return userMapper.toUserDto(user.get());
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> users = userListMapper.toUserDtoList(userStorage.getAll());
        log.info("Пользователи получены");
        return users;
    }

    @Override
    public void delete(int userId) {
        userStorage.delete(userId);
        log.info(String.format("Пользователь с id = %s удален", userId));

    }
}
