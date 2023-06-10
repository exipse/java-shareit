package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DBConstraintException;
import ru.practicum.shareit.exception.UserNoFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserListMapper;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validation.ValidateService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;
    private final ValidateService validateService;

    @Override
    public UserDto createUser(UserDto user) {
        try {
            User saveUser = userStorage.save(userMapper.toUserModel(user));
            log.info("Пользователь создан");
            return userMapper.toUserDto(saveUser);
        } catch (Throwable e) {
            throw new DBConstraintException("Ошибка валидации при добавлении в БД");
        }

    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        validateService.validateBeforeUpdateUser(user);
        try {
            UserDto noUpdateUser = get(id);
            if (!(user.getName() == null)) {
                noUpdateUser.setName(user.getName());
            }
            if (!(user.getEmail() == null)) {
                if (noUpdateUser.getEmail().equals(user.getEmail())) {
                    userStorage.delete(userMapper.toUserModel(user));
                }
                noUpdateUser.setEmail(user.getEmail());
            }
            User updateUser = userStorage.save(userMapper.toUserModel(noUpdateUser));
            log.info("Пользователь обновлен");
            return userMapper.toUserDto(updateUser);
        } catch (Throwable e) {
            throw new DBConstraintException("Ошибка валидации при добавлении в БД");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto get(Long id) {
        User user = userStorage.findById(id).orElseThrow(
                () -> new UserNoFoundException(String.format("Пользователь по id = %s не существует", id)));
        log.info(String.format("Пользователь по id = %s получен", id));
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        List<UserDto> users = userListMapper.toUserDtoList(userStorage.findAll());
        log.info("Пользователи получены");
        return users;
    }

    @Override
    public void delete(Long userId) {
        userStorage.deleteById(userId);
        log.info(String.format("Пользователь с id = %s удален", userId));

    }
}
