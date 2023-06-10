package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.enums.BookingState;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.mapper.ItemFullMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final UserStorage userStorage;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final ItemFullMapper itemFullMapper;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;


    @Override
    public BookingDto create(Long userId, BookingRequestDto book) {
        if (!userStorage.existsById(userId)) {
            throw new UserNoFoundException("Пользователя не существует");
        }
        if (!itemRepository.existsById(book.getItemId())) {
            throw new ItemNoFoundException("Вещи не существует");
        }
        if (book.getStart().isAfter(book.getEnd())
                || book.getStart().equals(book.getEnd())) {
            throw new DataTimeValidateException("Указано некорректное время бронирования");
        }
        User user = userMapper.toUserModel(userService.get(userId));
        Item item =
                itemFullMapper.itemFulltoModel(itemService.getById(book.getItemId(), userId));
        if (Objects.equals(item.getOwnerId(), userId)) {
            throw new BookingNoFoundException("Владелец вещи не может забронировать свою вещь");
        }
        if (item.getAvailable()) {
            Booking booking = Booking.builder()
                    .start(book.getStart())
                    .end(book.getEnd())
                    .item(item)
                    .booker(user)
                    .status(Status.WAITING)
                    .build();
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new NoAvailableException("Вещь не доступна для бронирования");
        }
    }

    @Override
    public BookingDto confirmOrRejectRequest(Long userId, Long bookingId, Boolean approved) {
        BookingDto booking = getInfoByBook(userId, bookingId);
        if (!Objects.equals(booking.getItem().getOwnerId(), userId)) {
            throw new UserNoFoundException(
                    String.format("Пользователь id = %s не является владельцем вещи которую бронируют", userId));
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new NoAvailableException(String.format("Вещь уже забронирована", userId));
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            bookingRepository.save(bookingMapper.toBookingModel(booking));
            return booking;
        }
        booking.setStatus(Status.REJECTED);
        bookingRepository.save(bookingMapper.toBookingModel(booking));
        return booking;

    }

    @Override
    public BookingDto getInfoByBook(Long userId, Long bookingId) {
        if (!userStorage.existsById(userId)) {
            throw new UserNoFoundException("Пользователя не существует");
        }
        if (!bookingRepository.existsById(bookingId)) {
            throw new BookingNoFoundException(String.format("Бронирование по id = %s не существует", bookingId));
        }
        Booking book = bookingRepository.checkInfoByBook(bookingId, userId).orElseThrow(
                () -> new UserNoFoundException("Пользователь не является автором бронирования или владельцем"));
        return bookingMapper.toBookingDto(book);
    }


    @Override
    public List<BookingDto> getAllBooksByUser(Long userId, String state, int from, int size) {

        User userFromDB = userMapper.toUserModel(userService.get(userId));
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime timeNow = LocalDateTime.now();

        BookingState bookingState;
        List<Booking> bookings = new ArrayList<>();
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedException(String.format("Unknown state: %s", state));
        }

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findALLUserBookings(userFromDB, pageable);
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userFromDB, timeNow,
                                timeNow, pageable);
                break;
            case PAST:
                bookings = bookingRepository
                        .findAllByBookerAndEndIsBeforeOrderByStartDesc(userFromDB, timeNow, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findAllByBookerAndStartIsAfterOrderByStartDesc(userFromDB, timeNow, pageable);
                break;
            case WAITING:
                bookings = bookingRepository
                        .findAllByBookerAndStatusEqualsOrderByStartDesc(userFromDB, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findAllByBookerAndStatusEqualsOrderByStartDesc(userFromDB, Status.REJECTED, pageable);
                break;
        }
        return bookingMapper.toBookingListDto(bookings);

    }

    @Override
    public List<BookingDto> getAllBooksByOwner(Long userId, String state, int from, int size) {
        if (!userStorage.existsById(userId)) {
            throw new UserNoFoundException("Пользователя не существует");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime timeNow = LocalDateTime.now();

        BookingState bookingState;
        List<Booking> bookings = new ArrayList<>();
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedException(String.format("Unknown state: %s", state));
        }

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerAll(userId, pageable);
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findCurrentBookingsByOwner(userId, timeNow, timeNow, pageable);
                break;
            case PAST:
                bookings = bookingRepository
                        .findPastBookingsByOwner(userId, timeNow, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findFutureBookingsByOwner(userId, timeNow, pageable);
                break;
            case WAITING:
                bookings = bookingRepository
                        .findAllByBookerByStatusOwner(userId, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findAllByBookerByStatusOwner(userId, Status.REJECTED, pageable);
                break;
        }
        return bookingMapper.toBookingListDto(bookings);
    }
}
