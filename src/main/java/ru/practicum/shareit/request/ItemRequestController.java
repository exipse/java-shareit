package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@Validated
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    /**
     * Добавить новый запрос вещи
     */
    @PostMapping
    public ItemRequestDto addNewRequest(@RequestBody @Valid ItemRequestDto requestDto,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST /requests. X-Sharer-User-Id = {}", userId);
        return requestService.addNewRequest(requestDto, userId);
    }

    /**
     * Получить список своих запросов вместе с данными об ответах на них
     */
    @GetMapping
    public List<ItemWithAnswersRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get /items. X-Sharer-User-Id = {}", userId);
        return requestService.getOwnRequests(userId);
    }


    /**
     * Получить список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public List<ItemWithAnswersRequestDto> getUserRequests(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                           @Positive @RequestParam(defaultValue = "10") int size,
                                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST /requests/all?from={}&size={}.  X-Sharer-User-Id = {}", from, size, userId);
        return requestService.getUserRequests(from, size, userId);
    }

    /**
     * Получить данные об одном конкретном запросе вместе с данными об ответах на него
     */
    @GetMapping("{requestId}")
    public ItemWithAnswersRequestDto getRequestById(@PathVariable Long requestId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get /requests/requestId. X-Sharer-User-Id = {}", userId);
        return requestService.getRequestById(requestId, userId);
    }
}
