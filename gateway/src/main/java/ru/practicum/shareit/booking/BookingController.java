package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated

public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid BookingRequestDto book) {
        return bookingClient.create(userId, book);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object>  confirmOrRejectRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) {
        return bookingClient.confirmOrRejectRequest(userId, bookingId, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     */

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object>  getInfoByBook(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingClient.getInfoByBook(userId, bookingId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя. Эндпоинт
     */
    @GetMapping
    public ResponseEntity<Object>  getAllBooksByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size) {
        return bookingClient.getAllBooksByUser(userId, state, from, size);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя.
     */
    @GetMapping("/owner")
    public ResponseEntity<Object>  getAllBooksByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @Positive @RequestParam(defaultValue = "10") int size) {
        return bookingClient.getAllBooksByOwner(userId, state, from, size);
    }
}
