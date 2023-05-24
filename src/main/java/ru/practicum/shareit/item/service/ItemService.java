package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto item, int userId);

    ItemDto updateItem(int itemId, int userId, ItemDto item);

    ItemFullDto getById(int itemId, int userId);

    List<ItemFullDto> getAllItemsByUser(int userId);

    List<ItemDto> search(String text);

    CommentDto addComment(int userId, CommentDto comment, int itemId);
}
