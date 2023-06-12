package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingShortMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingShortMapperTest {

    private final BookingShortDto bookingShortDto = new BookingShortDto();
    private final BookingShortDto bookingShortDto1 = BookingShortDto.builder()
            .id(1L)
            .start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L))
            .bookerId(1L).build();
    private final Item item = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    private final User user = new User(1L, "user1", "user1@user.com");
    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L))
            .item(item)
            .booker(user)
            .status(Status.WAITING).build();
    private final User user2 = new User(null, "user1", "user1@user.com");
    private final Booking booking2 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L))
            .item(item)
            .booker(user2)
            .status(Status.WAITING).build();
    private final Booking booking3 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L))
            .item(item)
            .booker(null)
            .status(Status.WAITING).build();
    private final BookingShortMapper mapper
            = Mappers.getMapper(BookingShortMapper.class);

    @Test
    public void toBookingShortDtoTest() {
        BookingShortDto shortDto = mapper.toBookingShortDto(booking);
        assertEquals(booking.getBooker().getId(), shortDto.getBookerId());
        assertEquals(booking.getStart(), shortDto.getStart());
    }

    @Test
    public void toBookingShortDtoTestNull() {
        BookingShortDto shortDto = mapper.toBookingShortDto(null);
        assertNull(shortDto);
    }

    @Test
    public void toBookingShortDtoTestNullBookerId() {
        BookingShortDto shortDto = mapper.toBookingShortDto(booking2);
        assertEquals(booking2.getBooker().getId(), shortDto.getBookerId());
    }

    @Test
    public void toBookingShortDtoTestNullBooker() {
        BookingShortDto shortDto = mapper.toBookingShortDto(booking3);
        assertNull(shortDto.getBookerId());
    }
}