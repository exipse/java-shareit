package ru.practicum.shareit.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class ValidateService {

    public void validateBeforeUpdateUser(UserDto user) {
        if (user.getName() != null) {
            if (user.getName().isBlank()) {
                throw new ValidateException("Имя пользователя не может быть путым");
            }
        }
        if (user.getEmail() != null) {
            if (user.getEmail().isBlank()) {
                throw new ValidateException("Почта не может быть пустой");
            }
        }
    }
}
