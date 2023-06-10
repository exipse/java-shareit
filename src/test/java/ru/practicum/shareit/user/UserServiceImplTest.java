package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.DBConstraintException;
import ru.practicum.shareit.exception.UserNoFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserListMapper;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validation.ValidateService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @Mock
    UserStorage userStorage;
    @Mock
    UserMapper userMapper;
    @Mock
    UserListMapper userListMapper;
    @Mock
    ValidateService validateService;

    @InjectMocks
    UserServiceImpl userService;

    private UserDto userDto1;
    private UserDto userDto2;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void before() {
        userDto1 = new UserDto(1L, "user1", "user1@user.com");
        user1 = new User(1L, "user1", "user1@user.com");
        userDto2 = new UserDto(2L, "user2", "user2@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        user3 = new User();
        Mockito.when(userMapper.toUserModel(userDto1)).thenReturn(user1);
        Mockito.when(userMapper.toUserDto(user1)).thenReturn(userDto1);
        Mockito.when(userMapper.toUserModel(userDto2)).thenReturn(user2);
        Mockito.when(userMapper.toUserDto(user2)).thenReturn(userDto2);
        Mockito.when(userListMapper.toUserDtoList(anyList())).thenReturn(List.of(userDto1, userDto2));
    }

    @Test
    void createUser() {
        User user = new User(1L, "user", "user@user.com");
        when(userStorage.save(any())).thenReturn(user);
        Mockito.when(userMapper.toUserDto(user)).thenReturn(userDto1);
        assertEquals(userService.createUser(userDto1), userDto1);
    }

    @Test
    void createUserDbConstraintException() {
        User user = new User(1L, "user", "user@user.com");
        when(userStorage.save(any())).thenThrow(new DBConstraintException("Ошибка валидации при добавлении в БД"));

        Exception exception = assertThrows(DBConstraintException.class,
                () -> userService.createUser(userDto1));
        assertEquals("Ошибка валидации при добавлении в БД", exception.getMessage());
    }

    @Test
    void updateFoundUser() {
        UserDto updateDto = new UserDto(1L, "user1UPD", "userUPD@user.com");
        when(userStorage.findById(1L)).thenReturn(Optional.of(user1));
        when(userMapper.toUserDto(any())).thenReturn(updateDto);
        assertEquals(userService.updateUser(1L, updateDto), updateDto);
    }

    @Test
    void updateNoFoundUser() {
        when(userStorage.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNoFoundException.class, () -> userService.get(1L));
    }

    @Test
    void getNoExistUserById() {
        assertThrows(UserNoFoundException.class, () -> userService.get(1L));
    }

    @Test
    void getUserById() {
        when(userStorage.findById(2L)).thenReturn(Optional.of(user2));
        assertEquals(userService.get(2L), userDto2);
    }

    @Test
    void getAllTwoUsers() {
        when(userStorage.save(user1)).thenReturn(user1);
        when(userStorage.save(user2)).thenReturn(user2);
        assertEquals(userService.createUser(userDto1), userDto1);
        assertEquals(userService.createUser(userDto2), userDto2);
        assertEquals(userService.getAll(), List.of(userDto1, userDto2));
    }

    @Test
    void delete() {
        userService.delete(1L);
        verify(userStorage, Mockito.times(1)).deleteById(1L);
    }
}