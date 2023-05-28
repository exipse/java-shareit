package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;


public interface BookingService {

    BookingDto create(Long userId, BookingRequestDto book);

    BookingDto confirmOrRejectRequest(Long userId, Long bookingId, Boolean approved);

    BookingDto getInfoByBook(Long userId, Long bookingId);

    List<BookingDto> getAllBooksByUser(Long userId, String state);

    List<BookingDto> getAllBooksByOwner(Long userId, String state);
}
