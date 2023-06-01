package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated

public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody @Valid BookingRequestDto book) {
        log.info("POST /bookings. X-Sharer-User-Id = {}", userId);
        return bookingService.create(userId, book);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto confirmOrRejectRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) {
        log.info("Patch /bookings/{}/{}. X-Sharer-User-Id = {}", bookingId, approved, userId);
        return bookingService.confirmOrRejectRequest(userId, bookingId, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     */

    @GetMapping("/{bookingId}")
    public BookingDto getInfoByBook(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Get /bookings/{}", bookingId);
        return bookingService.getInfoByBook(userId, bookingId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя. Эндпоинт
     */
    @GetMapping
    public List<BookingDto> getAllBooksByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Get /bookings?state={}", state);
        return bookingService.getAllBooksByUser(userId, state, from, size);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя.
     */
    @GetMapping("/owner")
    public List<BookingDto> getAllBooksByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Get /bookings/owner?state={}", state);
        return bookingService.getAllBooksByOwner(userId, state, from, size);
    }
}
