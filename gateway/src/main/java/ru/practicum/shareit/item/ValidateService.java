package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
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

    public void validateBeforeUpdateItem(ItemDto item) {
        if (item.getName() != null) {
            if (item.getName().isBlank()) {
                throw new ValidateException("Имя вещи не может быть путым");
            }
        }
        if (item.getDescription() != null) {
            if (item.getDescription().isBlank()) {
                throw new ValidateException("Описание вещи не может быть путым");
            }
        }

    }
}
