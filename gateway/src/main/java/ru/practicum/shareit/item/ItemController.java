package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;
    private final ValidateService validateService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemDto item) {
        validateService.validateBeforeUpdateItem(item);
        return itemClient.update(itemId, userId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.get(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid CommentDto commentDto,
                                             @PathVariable Long itemId) {
        return itemClient.addComment(userId, commentDto, itemId);
    }

}
