package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapping;
import ru.practicum.shareit.request.mapper.RequestMappingImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RequestMappingImpl.class, UserMapperImpl.class})
class RequestMappingTest {

    User user = new User(1L, "user1", "user1@user.com");
    UserDto userDto = new UserDto(1L, "user1", "user1@user.com");
    LocalDateTime localDateTime = LocalDateTime.now();
    ItemShortForRequestDto itemShortForRequestDto = new ItemShortForRequestDto(1L, "Дрель", 1L,
            "Простая дрель", true, 1L);
    ItemWithAnswersRequestDto itemWithAnswersRequestDto = new ItemWithAnswersRequestDto(1L, "Тумба",
            localDateTime, userDto, List.of(itemShortForRequestDto));
    ItemShortForRequestDto itemShortForRequestDto1 = new ItemShortForRequestDto();
    ItemWithAnswersRequestDto itemWithAnswersRequestDto1 = new ItemWithAnswersRequestDto();
    @Autowired
    private RequestMapping requestMapping;

    @Test
    public void toRequestDtoTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "Тумба", user, localDateTime);

        ItemRequestDto itemRequestDto = requestMapping.toRequestDto(itemRequest);
        assertEquals(itemRequest.getRequestor().getName(), itemRequestDto.getRequestor().getName());
    }

    @Test
    public void toRequestModelTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Тумба", localDateTime, userDto);
        ItemRequest itemRequest = requestMapping.toRequestModel(itemRequestDto);

        assertEquals(itemRequestDto.getRequestor().getName(), itemRequest.getRequestor().getName());
        assertEquals(itemRequestDto.getId(), itemRequest.getId());
    }


    @Test
    public void toRequestWithAnswerDtoTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Тумба");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(localDateTime);
        ItemWithAnswersRequestDto itemRequestDto = requestMapping.toRequestWithAnswerDto(itemRequest);
        assertEquals(itemRequest.getRequestor().getName(), itemRequestDto.getRequestor().getName());
    }

    @Test
    public void toRequestWithAnswerDtoTestNull() {
        ItemWithAnswersRequestDto itemRequestDto = requestMapping.toRequestWithAnswerDto(null);
        assertNull(itemRequestDto);
    }

    @Test
    public void toRequestWithAnswerDtoTestList() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Тумба");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(localDateTime);

        List<ItemWithAnswersRequestDto> itemRequestDto = requestMapping.toRequestWithAnswerListDto(List.of(itemRequest));

        assertEquals(itemRequest.getRequestor().getName(), itemRequestDto.get(0).getRequestor().getName());
    }

}