package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.mapper.ItemFullMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
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
    @Transactional
    public BookingDto create(Long userId, BookingRequestDto book) {
        userStorage.findById(userId)
                .orElseThrow(() -> new UserNoFoundException("Пользователя не существует"));
        itemRepository.findById(book.getItemId())
                .orElseThrow(() -> new ItemNoFoundException("Пользователя не существует"));
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
    @Transactional
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
    @Transactional
    public BookingDto getInfoByBook(Long userId, Long bookingId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new UserNoFoundException("Пользователя не существует"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNoFoundException(String.format("Бронирование по id = %s не существует", bookingId)));
        Booking book = bookingRepository.checkInfoByBook(bookingId, userId).orElseThrow(
                () -> new UserNoFoundException("Пользователь не является автором бронирования или владельцем"));
        return bookingMapper.toBookingDto(book);
    }


    @Override
    @Transactional
    public List<BookingDto> getAllBooksByUser(Long userId, String state) {

        User userFromDB = userMapper.toUserModel(userService.get(userId));
       // ZoneId zone = ZoneId.of("Europe/Moscow");
      //  Clock clock = Clock.systemUTC().withZone(zone);
        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case "ALL":
                return bookingMapper.toBookingListDto(bookingRepository.findALLUserBookings(userFromDB));
            case "CURRENT":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userFromDB, timeNow, timeNow));
            case "PAST":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerAndEndIsBeforeOrderByStartDesc(userFromDB, timeNow));
            case "FUTURE":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerAndStartIsAfterOrderByStartDesc(userFromDB, timeNow));
            case "WAITING":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerAndStatusEqualsOrderByStartDesc(userFromDB, Status.WAITING));
            case "REJECTED":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerAndStatusEqualsOrderByStartDesc(userFromDB, Status.REJECTED));
        }
        throw new UnsupportedException(String.format("Unknown state: %s", state));
    }

    @Override
    @Transactional
    public List<BookingDto> getAllBooksByOwner(Long userId, String state) {

        UserDto user = userService.get(userId);
      //  ZoneId zone = ZoneId.of("Europe/Moscow");
       // Clock clock = Clock.systemUTC().withZone(zone);
        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case "ALL":
                List<Booking> b = bookingRepository.findAllByBookerAll(userId);
                return bookingMapper.toBookingListDto(b);
            case "CURRENT":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findCurrentBookingsByOwner(userId, timeNow, timeNow));
            case "PAST":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findPastBookingsByOwner(userId, timeNow));
            case "FUTURE":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findFutureBookingsByOwner(userId, timeNow));
            case "WAITING":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerByStatusOwner(userId, Status.WAITING));
            case "REJECTED":
                return bookingMapper.toBookingListDto(bookingRepository
                        .findAllByBookerByStatusOwner(userId, Status.REJECTED));
        }
        throw new UnsupportedException(String.format("Unknown state: %s", state));
    }
}
