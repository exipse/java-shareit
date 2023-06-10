package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info("POST /users");
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto user, @PathVariable long userId) {
        log.info("PATCH /users/{}", userId);
        return userService.updateUser(userId, user);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable("userId") Long id) {
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
    public void delete(@PathVariable Long userId) {
        log.info("DELETE /users/{}", userId);
        userService.delete(userId);
    }
}
