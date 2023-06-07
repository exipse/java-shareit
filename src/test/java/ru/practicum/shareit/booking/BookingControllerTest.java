package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;

    private final UserDto userDto1 = new UserDto(1L, "user1", "user1@user.com");
    private final ItemDto itemDto1 = new ItemDto(1L, "Дрель", "Простая дрель",
            true, 1L, 1L);

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L))
            .item(itemDto1)
            .booker(userDto1)
            .status(Status.WAITING).build();

    private final BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusHours(1L))
            .end(LocalDateTime.now().plusHours(3L))
            .build();

    @Test
    void create() throws Exception {

        when(bookingService.create(anyLong(), any()))
                .thenReturn(bookingDto);

        String result = mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()))).andReturn()
                .getResponse().getContentAsString();

    }

    @Test
    void confirmOrRejectRequest() throws Exception {

        when(bookingService.confirmOrRejectRequest(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.item.description").value(bookingDto.getItem().getDescription()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.getBooker().getName()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).confirmOrRejectRequest(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getInfoByBook() throws Exception {

        when(bookingService.getInfoByBook(anyLong(), anyLong())).thenReturn(bookingDto);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.item.description").value(bookingDto.getItem().getDescription()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.getBooker().getName()));

    }

    @Test
    void getAllBooksByUser() throws Exception {
        Mockito.when(bookingService.getAllBooksByUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings", bookingDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].item.description").value(bookingDto.getItem().getDescription()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()));
    }

    @Test
    void getAllBooksByOwner() throws Exception {

        Mockito.when(bookingService.getAllBooksByOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner", bookingDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].item.description").value(bookingDto.getItem().getDescription()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()));
    }
}