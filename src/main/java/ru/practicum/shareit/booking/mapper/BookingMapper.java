package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {

    BookingDto toBookingDto(Booking booking);

    Booking toBookingModel(BookingDto booking);

    List<BookingDto> toBookingListDto(List<Booking> booking);

    List<Booking> toBookingListModel(List<BookingDto> booking);

}
