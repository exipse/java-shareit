package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ItemMapperList {

    List<Item> toItemModelList(List<ItemDto> itemDtoList);

    List<ItemDto> toItemDtoList(List<Item> item);
}
