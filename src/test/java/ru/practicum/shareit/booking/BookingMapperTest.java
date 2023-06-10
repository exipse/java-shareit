package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BookingMapperImpl.class, ItemMapperImpl.class, UserMapperImpl.class})
class BookingMapperTest {

    private final Item item = Item.builder().id(1L).name("Дрель").description("Простая дрель")
            .available(true).ownerId(1L).build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L).name("Дрель").description("Простая дрель").available(true).ownerId(1L).requestId(1L).build();
    private final User user = User.builder()
            .id(1L).name("user1").email("user1@user.com").build();
    private final UserDto userDto = UserDto.builder().id(1L).name("user1").email("user1@user.com").build();
    private final Booking booking = Booking.builder()
            .id(1L).start(LocalDateTime.now().minusHours(7L))
            .end(LocalDateTime.now().minusHours(5L)).item(item).booker(user).status(Status.WAITING).build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L).start(LocalDateTime.now().minusHours(7L)).end(LocalDateTime.now().minusHours(5L))
            .item(itemDto).booker(userDto).status(Status.WAITING).build();
    private final BookingDto bookingDto1 = new BookingDto();
    Booking booking1 = new Booking();
    @Autowired
    private BookingMapper mapper;

    @Test
    public void toBookingDtoTest() {
        BookingDto bookingDto = mapper.toBookingDto(booking);
        bookingDto1.setId(2L);
        assertEquals(booking.getItem().getName(), bookingDto.getItem().getName());
        assertEquals(booking.getStart(), bookingDto.getStart());
    }

    @Test
    public void toBookingListDto() {
        List<BookingDto> bookingDtos = mapper.toBookingListDto(List.of(booking));
        assertEquals(booking.getItem().getName(), bookingDtos.get(0).getItem().getName());
        assertEquals(booking.getStart(), bookingDtos.get(0).getStart());
        assertEquals(1, bookingDtos.size());
    }

    @Test
    public void toBookingListDtoNull() {
        List<Booking> bookingDtoList = null;
        List<BookingDto> bookingDtos = mapper.toBookingListDto(bookingDtoList);
        assertNull(bookingDtos);
    }

    @Test
    public void toBookingDtoTestNull() {
        BookingDto bookingDto = mapper.toBookingDto(null);
        assertNull(bookingDto);
    }

    @Test
    public void toBookingModelTest() {
        Booking booking = mapper.toBookingModel(bookingDto);
        assertEquals(bookingDto.getItem().getName(), booking.getItem().getName());
        assertEquals(bookingDto.getStart(), booking.getStart());
    }

    @Test
    public void toBookingModelTestNull() {
        Booking booking = mapper.toBookingModel(null);
        assertNull(booking);
    }
}