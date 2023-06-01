package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface RequestMapping {

    ItemRequestDto toRequestDto(ItemRequest itemRequest);

    ItemRequest toRequestModel(ItemRequestDto itemRequest);


    ItemWithAnswersRequestDto toRequestWithAnswerDto(ItemRequest itemRequest);


    List<ItemWithAnswersRequestDto> toRequestWithAnswerListDto(List<ItemRequest> itemRequest);

    List<ItemRequestDto> toRequestDtoList(List<ItemRequest> itemRequest);
}
