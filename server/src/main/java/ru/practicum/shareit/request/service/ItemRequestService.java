package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addNewRequest(ItemRequestDto requestDto, Long userId);

    List<ItemWithAnswersRequestDto> getOwnRequests(Long userId);

    List<ItemWithAnswersRequestDto> getUserRequests(int from, int size, Long userId);

    ItemWithAnswersRequestDto getRequestById(Long requestId, Long userId);
}
