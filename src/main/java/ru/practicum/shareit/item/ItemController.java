package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.ValidateService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ValidateService validateService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST /items. X-Sharer-User-Id = {}", userId);
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId,
                          @RequestBody ItemDto item) {
        log.info("PATCH /items/{} X-Sharer-User-Id = {}", itemId, userId);
        validateService.validateBeforeUpdateItem(item);
        return itemService.updateItem(itemId, userId, item);
    }

    @GetMapping("/{itemId}")
    public ItemFullDto get(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET /items/{}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemFullDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET /items  X-Sharer-User-Id = {}", userId);
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET /items/search?text={}", text);
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @RequestBody @Valid CommentDto commentDto,
                                 @PathVariable int itemId) {
        log.info("POST /items/{}/comment", itemId);
        return itemService.addComment(userId, commentDto, itemId);
    }

}
