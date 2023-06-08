package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper mapper
            = Mappers.getMapper(UserMapper.class);

    @Test
    public void toUserModelTest() {

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("SourceDescription");
        User user = mapper.toUserModel(userDto);

        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), userDto.getEmail());
    }

    @Test
    public void toUserModelTestNull() {

        User user = mapper.toUserModel(null);

        assertNull(user);
    }


    @Test
    public void toUserDtoTest() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("SourceDescription");
        UserDto userDto = mapper.toUserDto(user);

        assertEquals(user.getName(), userDto.getName());
        assertEquals(userDto.getEmail(), userDto.getEmail());
    }

    @Test
    public void toUserDtoTestNull() {
        UserDto userDto = mapper.toUserDto(null);

        assertNull(userDto);
    }
}