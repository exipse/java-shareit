package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestServiceImpl requestService;

    UserDto userDto = new UserDto(1L, "user1", "user1@user.com");
    ItemRequestDto requestDto = new ItemRequestDto(1L, "Запрос", LocalDateTime.now(), userDto);
    ItemShortForRequestDto itemShortForRequestDto = new ItemShortForRequestDto(1L, "Дрель", 1L,
            "Простая дрель", true, 1L);
    List<ItemShortForRequestDto> itemShortForRequestDtos = List.of(itemShortForRequestDto);
    ItemWithAnswersRequestDto answersRequestDto = new ItemWithAnswersRequestDto(1L, "Простая дрель",
            LocalDateTime.now(), userDto, itemShortForRequestDtos);

    @Test
    void addNewRequest() throws Exception {

        when(requestService.addNewRequest(any(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.requestor.name", is(requestDto.getRequestor().getName())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    void getOwnRequests() throws Exception {
        when(requestService.getOwnRequests(anyLong()))
                .thenReturn(List.of(answersRequestDto));

        mvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(answersRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(answersRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].requestor.name", is(answersRequestDto.getRequestor().getName())));
    }

    @Test
    void getUserRequests() throws Exception {
        when(requestService.getUserRequests(anyInt(), anyInt(), anyLong())).thenReturn(List.of(answersRequestDto));

        mvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(answersRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(answersRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].requestor.name", is(answersRequestDto.getRequestor().getName())));
    }

    @Test
    void getRequestById() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(answersRequestDto);

        mvc.perform(MockMvcRequestBuilders.get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(answersRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(answersRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.requestor.name", is(answersRequestDto.getRequestor().getName())));

    }
}