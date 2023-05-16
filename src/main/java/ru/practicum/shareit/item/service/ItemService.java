package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto item, int userId);

    ItemDto updateItem(int itemId, int userId, ItemDto item);

    ItemDto getById(int itemId);

    List<ItemDto> getAllItemsByUser(int userId);

    List<ItemDto> search(String text);
}
