package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.comment.mapper.CommetMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommetMapper.class, BookingMapper.class,})
public interface ItemMapper {

    Item toItemModel(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    List<Item> toItemModelList(List<ItemDto> itemDtoList);

    List<ItemDto> toItemDtoList(List<Item> item);
}
