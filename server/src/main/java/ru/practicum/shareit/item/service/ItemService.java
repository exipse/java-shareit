package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto item, Long userId);

    ItemDto updateItem(Long itemId, Long userId, ItemDto item);

    ItemFullDto getById(Long itemId, Long userId);

    List<ItemFullDto> getAllItemsByUser(Long userId);

    List<ItemDto> search(String text);

    CommentDto addComment(Long userId, CommentDto comment, Long itemId);
}
