package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserNoFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info("POST /users");
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto user, @PathVariable String userId) {
        log.info("PATCH /users/{}", userId);
        int id = Integer.parseInt(userId);
        if (userService.get(id) == null) {
            throw new UserNoFoundException(String.format("Пользователя с id %s не существует", id));
        }
        return userService.updateUser(id, user);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable("userId") int id) {
        log.info("GET /users/{}", id);
        return userService.get(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int userId) {
        log.info("DELETE /users/{}", userId);
        userService.delete(userId);
    }
}
