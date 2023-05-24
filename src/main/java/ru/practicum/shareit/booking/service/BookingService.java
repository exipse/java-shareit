package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;


public interface BookingService {

    BookingDto create(int userId,InputBookingDto book);

    BookingDto validateRequest(int userId, int bookingId, Boolean approved);

    BookingDto getInfoByBook(int userId, int bookingId);

    List<BookingDto> getAllBooksByUser(int userId, String state);

    List<BookingDto> getAllBooksByOwner(int userId, String state);
}
