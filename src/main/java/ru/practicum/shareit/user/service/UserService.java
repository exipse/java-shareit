package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto updateUser(int id, UserDto user);

    UserDto get(int id);

    List<UserDto> getAll();

    void delete(int userId);
}
