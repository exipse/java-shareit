package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.RequestNoFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapping;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestStorage requestStorage;
    private final ItemRepository itemStorage;
    private final RequestMapping requestMapping;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto addNewRequest(ItemRequestDto requestDto, Long userId) {
        UserDto user = userService.get(userId);
        LocalDateTime dateTime = LocalDateTime.now();
        requestDto.setRequestor(user);
        requestDto.setCreated(dateTime);
        ItemRequest requestModel = requestMapping.toRequestModel(requestDto);
        requestStorage.save(requestModel);
        return requestMapping.toRequestDto(requestModel);
    }

    @Override
    @Transactional
    public List<ItemWithAnswersRequestDto> getOwnRequests(Long userId) {
        userService.get(userId);
        List<ItemRequest> requestList = requestStorage.findAllByRequestor_IdOrderByCreatedDesc(userId);
        List<ItemWithAnswersRequestDto> reqWithAns = requestMapping.toRequestWithAnswerListDto(requestList);
        if (requestList != null) {
            return reqWithAns.stream().map(this::enrichmentRequestAnswerDto).collect(Collectors.toList());
        }
        return reqWithAns;
    }

    @Override
    @Transactional
    public List<ItemWithAnswersRequestDto> getUserRequests(int from, int size, Long userId) {
        userService.get(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> requestList = requestStorage.findAllByRequestor_IdIsNotOrderByCreated(userId, pageable);
        List<ItemWithAnswersRequestDto> requestDtoListList = requestMapping.toRequestWithAnswerListDto(requestList);
        if (requestList != null) {
            return requestDtoListList.stream().map(this::enrichmentRequestAnswerDto).collect(Collectors.toList());
        }
        return requestDtoListList;
    }

    @Override
    @Transactional
    public ItemWithAnswersRequestDto getRequestById(Long requestId, Long userId) {
        ItemRequest requestInDb = requestStorage.findById(requestId).orElseThrow(() ->
                new RequestNoFoundException(String.format("Запроса по id = %s не существует", requestId)));
        userService.get(userId);
        return enrichmentRequestAnswerDto(requestMapping.toRequestWithAnswerDto(requestInDb));
    }

    private ItemWithAnswersRequestDto enrichmentRequestAnswerDto(ItemWithAnswersRequestDto dto) {
        dto.setItems(itemMapper.toItemShortForReqDtos(itemStorage
                .findAllByRequestId(dto.getId())));
        return dto;
    }

}
