package ru.practicum.shareit.item.comment.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    int id;
    @NotBlank
    String text;
    ItemDto item;
    String authorName;
    private LocalDateTime created;
}
