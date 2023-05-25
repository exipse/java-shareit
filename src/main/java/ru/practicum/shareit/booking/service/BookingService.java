package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;


public interface BookingService {

    BookingDto create(Long userId,InputBookingDto book);

    BookingDto validateRequest(Long userId, Long bookingId, Boolean approved);

    BookingDto getInfoByBook(Long userId, Long bookingId);

    List<BookingDto> getAllBooksByUser(Long userId, String state);

    List<BookingDto> getAllBooksByOwner(Long userId, String state);
}
