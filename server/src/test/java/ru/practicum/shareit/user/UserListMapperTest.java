package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserListMapper;
import ru.practicum.shareit.user.mapper.UserListMapperImpl;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserListMapperImpl.class, UserMapperImpl.class})
class UserListMapperTest {

    @Autowired
    private UserListMapper mapper;

    @Test
    public void toUserDtoList() {

        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("SourceDescription");
        List<UserDto> userDtos = mapper.toUserDtoList(List.of(user));

        assertEquals(user.getName(), userDtos.get(0).getName());
        assertEquals(1, userDtos.size());
    }

    @Test
    public void toUserDtoListNull() {
        List<User> users = null;
        List<UserDto> userDtos = mapper.toUserDtoList(users);
        assertNull(userDtos);
    }

}