package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@Validated
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    /**
     * Добавить новый запрос вещи
     */
    @PostMapping
    public ResponseEntity<Object> addNewRequest(@RequestBody @Valid ItemRequestDto requestDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.addNewRequest(requestDto, userId);
    }

    /**
     * Получить список своих запросов вместе с данными об ответах на них
     */
    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getOwnRequests(userId);
    }


    /**
     * Получить список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getUserRequests(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                           @Positive @RequestParam(defaultValue = "10") int size,
                                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getUserRequests(from, size, userId);
    }

    /**
     * Получить данные об одном конкретном запросе вместе с данными об ответах на него
     */
    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getRequestById(requestId, userId);
    }
}
