package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.comment.mapper.CommetMapper;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommetMapper.class, UserMapper.class})
public interface ItemFullMapper {

     ItemFullDto itemFulltoDto(Item item);

     Item itemFulltoModel(ItemFullDto itemFullDto);

     List<ItemFullDto> itemFulltoDtoList(List<Item> item);
}
