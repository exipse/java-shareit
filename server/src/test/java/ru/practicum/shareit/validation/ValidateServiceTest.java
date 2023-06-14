package ru.practicum.shareit.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidateServiceTest {

    @InjectMocks
    private ValidateService validateService;

    UserDto userDto = new UserDto(1L, "", "user1@user.com");

    @Test
    void validateBeforeUpdateUser() {
        Exception exception = assertThrows(ValidateException.class,
                () -> validateService.validateBeforeUpdateUser(userDto));
        assertEquals(exception.getMessage(), "Имя пользователя не может быть путым");
    }
}