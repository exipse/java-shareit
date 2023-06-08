package ru.practicum.shareit.item.comment.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    Long id;
    @NotBlank
    String text;
    ItemDto item;
    String authorName;
    private LocalDateTime created;
}
