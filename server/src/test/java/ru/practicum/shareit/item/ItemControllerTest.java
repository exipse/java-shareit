package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.ValidateService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ValidateService validateService;

    private final ItemDto itemDto = new ItemDto(1L, "Instrument", "Just Instrument", true, 1L, 1L);
    private final CommentDto comment = new CommentDto(1L, "Comment", itemDto, "user1", LocalDateTime.now());
    private final CommentDto comment1 = CommentDto.builder()
            .text("Comment")
            .build();

    private final BookingShortDto bookingShortDto1 = BookingShortDto.builder()
            .id(1L)
            .start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L))
            .bookerId(1L).build();

    private final BookingShortDto bookingShortDto2 = BookingShortDto.builder()
            .id(2L)
            .start(LocalDateTime.now().plusHours(5L))
            .end(LocalDateTime.now().plusHours(7L))
            .bookerId(1L).build();

    private final List<CommentDto> commentListDto = List.of(comment);

    private final ItemFullDto fullDto = ItemFullDto.builder()
            .id(1L)
            .name("Дрель")
            .description("Description")
            .available(true)
            .ownerId(1L)
            .lastBooking(bookingShortDto1)
            .nextBooking(bookingShortDto2)
            .comments(commentListDto)
            .build();

    @Test
    void createitem() throws Exception {
        when(itemService.createItem(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", itemDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(fullDto);

        mvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", fullDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(fullDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(fullDto.getDescription())));
    }

    @Test
    void getAllItemsByUser() throws Exception {
        when(itemService.getAllItemsByUser(anyLong())).thenReturn(List.of(fullDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(fullDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(fullDto.getName()), String.class));

    }

    @Test
    void searchByDescription() throws Exception {
        when(itemService.search("something"))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "something")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("Just Instrument")));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(comment);

        String result = mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(comment1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(comment.getText())).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(comment), result);
    }
}